package com.infosys.fbp.platform.tsh.util;

import com.infosys.fbp.platform.tsh.model.FetchAndVerify;
import com.infosys.fbp.platform.tsh.model.TableMapper;
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
import java.util.List;
import java.util.Map;

import static com.infosys.fbp.platform.tsh.Constants.*;
import static com.infosys.fbp.platform.tsh.util.Note.addNoteToCell;
import static com.infosys.fbp.platform.tsh.util.TDGWorkbook.getSeparatorRow;

@Slf4j
public class TableWithNote {
    public static void addTableWithNote(TDGWorkbook workbook, String sheetName, String commandName, TableMapper tableMapper) {
        List<String> columnNameList = new ArrayList<>(tableMapper.getColumnNameMap().keySet());
        List<String> verifyCommandList = new ArrayList<>();

        columnNameList.forEach(columnName -> {
            if (verifyCommandList.isEmpty()) {
                verifyCommandList.add(tableMapper.getConcordionCommand()); // #ROW is applicable for 1st column only
            } else if (StringUtils.isNotBlank(tableMapper.getConcordionVerifyCommand(columnName))) {
                verifyCommandList.add(tableMapper.getConcordionVerifyCommand(columnName));
            } else {
                verifyCommandList.add("");
            }
        });
        getSeparatorRow(workbook, sheetName, commandName);
        addTableWithNote(workbook, sheetName, commandName, columnNameList, tableMapper.getData(), verifyCommandList);
    }

    public static void addTableWithNote(TDGWorkbook workbook, String sheetName, String commandName, FetchAndVerify fetchAndVerify) {
        getSeparatorRow(workbook, sheetName, commandName);
        addTableWithNote(workbook, sheetName, "Fetch-" + commandName, fetchAndVerify.getParameterList(), new ArrayList<>(), List.of(fetchAndVerify.getConcordionCommand()));
        addTableWithNote(workbook, sheetName, "Verify-" + commandName, fetchAndVerify.getResultColumnMap().keySet().stream().toList(), new ArrayList<>(), fetchAndVerify.getVerifyCommands());
    }

    public static void addTableWithNote(TDGWorkbook workbook, String sheetName,
                                        String tableName,
                                        List<String> tableColumnList,
                                        List<Map<String, String>> data,
                                        List<String> concordionCommandList) {
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


        // Create header row
        Row headerRow = sheet.createRow(lastRow++);
        for (int i = 0; i < tableColumnList.size(); i++) {
            Cell cell = headerRow.createCell(i + START_COLUMN);
            cell.setCellValue(tableColumnList.get(i));
            // Add note to cell if needed
            if (concordionCommandList.size() > i && StringUtils.isNotBlank(concordionCommandList.get(i))) {
                addNoteToCell(workbook, sheetName, cell, concordionCommandList.get(i));
            }
        }


        // Create data rows
        for (Map<String, String> datum : data) {
            Row dataRow = sheet.createRow(lastRow++);
            for (int j = 0; j < tableColumnList.size(); j++) {
                String header = tableColumnList.get(j);
                Cell cell = dataRow.createCell(j + START_COLUMN);
                cell.setCellValue(datum.get(header));
            }
        }

        // Auto-size columns
        for (int i = 0; i < tableColumnList.size(); i++) {
            sheet.autoSizeColumn(i);
        }

        // Create table
        log.debug("Start {}, End {} & {}", startRow, lastRow, tableColumnList.size());
        AreaReference tableArea = new AreaReference(new CellReference(startRow, START_COLUMN),
                new CellReference(lastRow - 1, tableColumnList.size() + START_COLUMN - 1), null);
        XSSFSheet xssfSheet = workbook.getSheetMap().get(sheetName);
        XSSFTable table = xssfSheet.createTable(tableArea);
        table.setName(tableName);

        CTTableStyleInfo styleInfo = table.getCTTable().addNewTableStyleInfo();
        styleInfo.setName("TableStyleMedium2");
        styleInfo.setShowColumnStripes(false);
        styleInfo.setShowRowStripes(true);

        workbook.getSheetLastRow().replace(sheetName, lastRow + data.size() + GAP_BETWEEN_COMMANDS);

    }
}
