package com.pratishthanventures.tdg.util;

import com.pratishthanventures.tdg.Constants;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import static com.pratishthanventures.tdg.Constants.START_ROW;


@Getter
public class TDGWorkbook {

    private XSSFWorkbook workbook;

    private final Map<String, XSSFSheet> sheetMap = new HashMap<>();
    private final Map<String, Integer> sheetLastRow = new HashMap<>();


    public TDGWorkbook(String sheetName) {
        createSheet(sheetName);
    }

    public void createSheet(String sheetName) {
        // Create a new workbook and sheet
        workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(sheetName);
        workbook.setSheetName(0, sheetName);
        sheetMap.put(sheetName, sheet);
        sheetLastRow.put(sheetName, START_ROW);
    }

    @SneakyThrows
    public void writeWorkbookToFile(String excelFilePath) {
        // Write workbook to file
        FileOutputStream fileOut = new FileOutputStream("target/" + excelFilePath);
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
    }

    public Row getNewRow(String sheetName) {
        return sheetMap.get(sheetName).createRow(sheetLastRow.get(sheetName));
    }

    public static CellStyle createGreenStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    public static void applyStyleToRow(Row row, CellStyle style) {
        // Apply style to all cells in the row, creating cells if they don't exist
        for (int i = 0; i < Constants.BACKGROUND_COLUMNS; i++) { // Adjust the number based on your needs
            Cell cell = row.getCell(i);
            if (cell == null) {
                cell = row.createCell(i);
            }
            cell.setCellStyle(style);
        }
    }

    public static void getSeparatorRow(TDGWorkbook workbook, String sheetName) {
        Row row = workbook.getNewRow(sheetName);
        applyStyleToRow(row, createGreenStyle(workbook.getWorkbook()));
        workbook.getSheetLastRow().replace(sheetName, workbook.getSheetLastRow().get(sheetName) + 1);
    }

}
