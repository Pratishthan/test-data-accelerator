import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonToExcelData {
    public static void main(String[] args) {
        try {
            // Read JSON file
            String content = new String(Files.readAllBytes(Paths.get("excelFragment.json")));
            JSONObject jsonObject = new JSONObject(content);

            // Get headers
            JSONArray headersArray = jsonObject.getJSONArray("headers");
            List<String> headers = new ArrayList<>();
            for (int i = 0; i < headersArray.length(); i++) {
                headers.add(headersArray.getString(i));
            }

            // Get data
            JSONArray dataArray = jsonObject.getJSONArray("data");
            List<Map<String, String>> dataRows = new ArrayList<>();

            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject rowObject = dataArray.getJSONObject(i);
                Map<String, String> rowMap = new HashMap<>();

                for (String header : headers) {
                    rowMap.put(header, rowObject.getString(header));
                }
                dataRows.add(rowMap);
            }

            // Create new Excel workbook in XSSF format
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Data");

            // Create header row
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers.get(i));

                // Optional: Apply bold formatting to header
                CellStyle headerStyle = workbook.createCellStyle();
                Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerStyle.setFont(headerFont);
                cell.setCellStyle(headerStyle);
            }

            // Create data rows
            for (int i = 0; i < dataRows.size(); i++) {
                Row row = sheet.createRow(i + 1);
                Map<String, String> rowData = dataRows.get(i);

                for (int j = 0; j < headers.size(); j++) {
                    String header = headers.get(j);
                    String value = rowData.get(header);
                    Cell cell = row.createCell(j);
                    cell.setCellValue(value);
                }
            }

            // Auto-size columns for better readability
            for (int i = 0; i < headers.size(); i++) {
                sheet.autoSizeColumn(i);
            }

            // Save the workbook
            try (FileOutputStream fileOut = new FileOutputStream("reconstructed_excel.xlsx")) {
                workbook.write(fileOut);
            }
            workbook.close();

            System.out.println("Excel workbook successfully reconstructed from JSON data.");

            // Return the workbook object for further processing if needed
            // This can be used elsewhere in your application
            // return workbook;

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Return null in case of failure (better to use proper exception handling in production)
        // return null;
    }

    // Method for other classes to use - returns an XSSFWorkbook from a JSON file
    public static XSSFWorkbook getExcelWorkbookFromJson(String jsonFilePath) {
        try {
            // Read JSON file
            String content = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
            JSONObject jsonObject = new JSONObject(content);

            // Get headers
            JSONArray headersArray = jsonObject.getJSONArray("headers");
            List<String> headers = new ArrayList<>();
            for (int i = 0; i < headersArray.length(); i++) {
                headers.add(headersArray.getString(i));
            }

            // Get data
            JSONArray dataArray = jsonObject.getJSONArray("data");
            List<Map<String, String>> dataRows = new ArrayList<>();

            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject rowObject = dataArray.getJSONObject(i);
                Map<String, String> rowMap = new HashMap<>();

                for (String header : headers) {
                    rowMap.put(header, rowObject.getString(header));
                }
                dataRows.add(rowMap);
            }

            // Create new Excel workbook in XSSF format
            XSSFWorkbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Data");

            // Create header row
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers.get(i));

                // Apply bold formatting to header
                CellStyle headerStyle = workbook.createCellStyle();
                Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerStyle.setFont(headerFont);
                cell.setCellStyle(headerStyle);
            }

            // Create data rows
            for (int i = 0; i < dataRows.size(); i++) {
                Row row = sheet.createRow(i + 1);
                Map<String, String> rowData = dataRows.get(i);

                for (int j = 0; j < headers.size(); j++) {
                    String header = headers.get(j);
                    String value = rowData.get(header);
                    Cell cell = row.createCell(j);
                    cell.setCellValue(value);
                }
            }

            // Auto-size columns for better readability
            for (int i = 0; i < headers.size(); i++) {
                sheet.autoSizeColumn(i);
            }

            return workbook;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}