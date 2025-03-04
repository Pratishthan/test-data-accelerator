package com.infosys.fbp.platform.tsh.util;

import com.infosys.fbp.platform.tsh.PropertyType;
import com.infosys.fbp.platform.tsh.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFTable;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyleInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.infosys.fbp.platform.tsh.Constants.*;
import static com.infosys.fbp.platform.tsh.util.Note.addNoteToCell;
import static com.infosys.fbp.platform.tsh.util.ParamCellList.addParamCellList;
import static com.infosys.fbp.platform.tsh.util.TDGWorkbook.getSeparatorRow;

@Slf4j
public class TableWithNote {

    public static void addTableWithNote(TDGWorkbook workbook, String sheetName, String commandName, TableMapper tableMapper) {
        List<String> columnNameList = new ArrayList<>(tableMapper.getColumnNameMap().keySet());
        List<String> verifyCommandList = new ArrayList<>();

        Map<String, String> columnWithCommandMap = new HashMap<>();

        columnNameList.forEach(columnName -> {
            if (verifyCommandList.isEmpty()) {
                columnWithCommandMap.put(columnName, tableMapper.getConcordionCommand());
            } else if (StringUtils.isNotBlank(tableMapper.getConcordionVerifyCommand(columnName))) {
                columnWithCommandMap.put(columnName, tableMapper.getConcordionVerifyCommand(columnName));
            } else {
                columnWithCommandMap.put(columnName, "");
            }
        });
        getSeparatorRow(workbook, sheetName, commandName);
        addTableWithNote(workbook, sheetName, commandName, columnWithCommandMap, tableMapper.getData());
    }

    public static void addTableWithNote(TDGWorkbook workbook, String sheetName, String commandName, PostAndVerify postAndVerify) {
        getSeparatorRow(workbook, sheetName, commandName);
        List<Parameter> paramColumnList = new ArrayList<>();
        paramColumnList.addAll(postAndVerify.getPropertyListMap().get(PropertyType.PathParamList).stream().map(Property::getParameter).toList());
        paramColumnList.addAll(postAndVerify.getPropertyListMap().get(PropertyType.QueryParamList).stream().map(Property::getParameter).toList());
        if (!paramColumnList.isEmpty()) {
            addParamCellList(workbook, sheetName, paramColumnList);
        }
        if (!postAndVerify.getPropertyListMap().get(PropertyType.RequestBodyColumnList).isEmpty()) {
            addTableWithNote(workbook, sheetName, "Request-" + commandName, postAndVerify.getColumnMapForRequest(), new ArrayList<>());
        }

        if (!postAndVerify.getPropertyListMap().get(PropertyType.ResponseBodyColumnList).isEmpty()) {
            addTableWithNote(workbook, sheetName, "Verify-" + commandName, postAndVerify.getColumnMapForVerify(), new ArrayList<>());
        }

    }

    public static void addTableWithNote(TDGWorkbook workbook, String sheetName, String commandName, FetchAndVerify fetchAndVerify) {
        getSeparatorRow(workbook, sheetName, commandName);
        List<Parameter> paramColumnList = new ArrayList<>();
        paramColumnList.addAll(fetchAndVerify.getPropertyListMap().get(PropertyType.PathParamList).stream().map(Property::getParameter).toList());
        paramColumnList.addAll(fetchAndVerify.getPropertyListMap().get(PropertyType.QueryParamList).stream().map(Property::getParameter).toList());
        if (!paramColumnList.isEmpty()) {
            addParamCellList(workbook, sheetName, paramColumnList);
        }
        if (!fetchAndVerify.getPropertyListMap().get(PropertyType.RequestBodyColumnList).isEmpty()) {
            addTableWithNote(workbook, sheetName, "Fetch-Request-" + commandName, fetchAndVerify.getColumnMapForRequest(), new ArrayList<>());
        }
        addTableWithNote(workbook, sheetName, "Verify-" + commandName, fetchAndVerify.getColumnMapForVerify(), new ArrayList<>());
    }

    public static void addTableWithNote(TDGWorkbook workbook, String sheetName,
                                        String tableName,
                                        Map<String, String> columnWithCommandMap,
                                        List<Map<String, String>> data) {
        // Empty row above

        Integer lastRow = workbook.getSheetLastRow().get(sheetName);

        lastRow += GAP_BETWEEN_COMMANDS;

        Integer startRow = lastRow;

        Sheet sheet = workbook.getWorkbook().getSheet(sheetName);

        if (ObjectUtils.isEmpty(data)) {
            data = new ArrayList<>();
            for (int i = 0; i < EMPTY_ROWS_IN_TABLE; i++) {
                data.add(Map.of("", ""));
            }
        }

        Map<String, Integer> columnIndex = new HashMap<>();

        // Create header row
        Row headerRow = sheet.createRow(lastRow++);
        AtomicInteger i = new AtomicInteger();
        columnWithCommandMap.forEach((col, command) -> {
            Cell cell = headerRow.createCell(i.get() + START_COLUMN);
            cell.setCellValue(col);
            if (StringUtils.isNotBlank(command)) {
                addNoteToCell(workbook, sheetName, cell, command);
            }
            columnIndex.put(col, i.get());
            i.getAndIncrement();
        });


        // Create data rows
        for (Map<String, String> datum : data) {

            Row dataRow = sheet.createRow(lastRow++);

            datum.forEach((key, value) -> {
                if (columnIndex.containsKey(key)) {
                    Integer idx = columnIndex.get(key);
                    Cell cell = dataRow.createCell(idx + START_COLUMN);
                    cell.setCellValue(value);
                } else {
                    log.error("{} not found", key);
                }
            });
        }


        // Create table
        log.debug("Start {}, End {} & {}", startRow, lastRow, columnWithCommandMap.size());
        AreaReference tableArea = new AreaReference(new CellReference(startRow, START_COLUMN),
                new CellReference(lastRow - 1, columnWithCommandMap.size() + START_COLUMN - 1), null);
        XSSFSheet xssfSheet = workbook.getSheetMap().get(sheetName);
        XSSFTable table = xssfSheet.createTable(tableArea);
        table.setName(tableName);

        CTTableStyleInfo styleInfo = table.getCTTable().addNewTableStyleInfo();
        styleInfo.setName("TableStyleMedium2");
        styleInfo.setShowColumnStripes(false);
        styleInfo.setShowRowStripes(true);

        workbook.getSheetLastRow().replace(sheetName, lastRow + data.size() + GAP_BETWEEN_COMMANDS);

        // Auto-size columns
        for (int j = 0; j < columnWithCommandMap.size(); j++) {
            sheet.autoSizeColumn(j);
        }

    }
}
