package com.pratishthanventures.tdg.output;

import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;


@Getter
public class TDGWorkbook {

    private XSSFWorkbook workbook;

    private final Map<String, Sheet> sheetMap = new HashMap<>();
    private final Map<String, Integer> sheetLastRow = new HashMap<>();


    public TDGWorkbook(String sheetName) {
        createSheet(sheetName);
    }

    public void createSheet(String sheetName) {
        // Create a new workbook and sheet
        workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(sheetName);
        workbook.setSheetName(0, sheetName);
        sheetMap.put(sheetName, sheet);
        sheetLastRow.put(sheetName, 0);
    }

    @SneakyThrows
    public void writeWorkbookToFile(String excelFilePath) {
        // Write workbook to file
        FileOutputStream fileOut = new FileOutputStream("target/" + excelFilePath);
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
    }


}
