package com.pratishthanventures.tdg.output;

import com.pratishthanventures.tdg.model.TableMapperHelper;
import lombok.extern.slf4j.Slf4j;
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

import static com.pratishthanventures.tdg.Constants.*;
import static com.pratishthanventures.tdg.output.Note.addNoteToCell;

@Slf4j
public class TableWithNote {

    public static void addTableWithNote(TDGWorkbook workbook, String sheetName, TableMapperHelper tableMapperHelper) {
        // Empty row above


        Integer lastRow = workbook.getSheetLastRow().get(sheetName);

        lastRow += GAP_BETWEEN_COMMANDS;

        Integer startRow = lastRow;

        Sheet sheet = workbook.getWorkbook().getSheet(sheetName);

        List<String> tableColumnList = tableMapperHelper.getColumnNameList();
        List<Map<String, String>> data = tableMapperHelper.getData();

        if (data.isEmpty()) {
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
            if (i == 0 && StringUtils.isNotBlank(tableMapperHelper.getConcordionCommand())) {
                addNoteToCell(workbook, sheetName, cell, tableMapperHelper.getConcordionCommand());
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
        log.info("Start {}, End {} & {}", startRow, lastRow, tableColumnList.size());
        AreaReference tableArea = new AreaReference(new CellReference(startRow, START_COLUMN),
                new CellReference(lastRow - 1, tableColumnList.size() + START_COLUMN - 1), null);
        XSSFSheet xssfSheet = workbook.getSheetMap().get(sheetName);
        XSSFTable table = xssfSheet.createTable(tableArea);
        table.setName(tableMapperHelper.getTableName());

        CTTableStyleInfo styleInfo = table.getCTTable().addNewTableStyleInfo();
        styleInfo.setName("TableStyleMedium2");
        styleInfo.setShowColumnStripes(false);
        styleInfo.setShowRowStripes(true);

        workbook.getSheetLastRow().replace(sheetName, lastRow + data.size() + GAP_BETWEEN_COMMANDS);

    }
}
