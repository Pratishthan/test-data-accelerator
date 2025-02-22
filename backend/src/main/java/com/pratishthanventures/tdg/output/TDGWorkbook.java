package com.pratishthanventures.tdg.output;

import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Row;
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

}
