import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFTable;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyleInfo;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static org.apache.poi.ss.usermodel.CellType.STRING;

@Slf4j
public class JsonToExcelConverter {

    private static Integer lastRow = 0;

    public static void convertJsonToExcel(String jsonFilePath, String excelFilePath) throws IOException {
        // Read JSON data from file
        ObjectMapper mapper = new ObjectMapper();
        String content = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
        Spec spec = mapper.readValue(content, Spec.class);

        // Create a new workbook and sheet
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");
        workbook.setSheetName(0, "Data");


        spec.getTables().forEach(tbl -> {
            createSingleTable(sheet, tbl, workbook);
        });

        createActionEntry(sheet, spec.getAction(), workbook);

        // Write workbook to file
        FileOutputStream fileOut = new FileOutputStream("target/" + excelFilePath);
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
    }

    private static void createActionEntry(Sheet sheet, String action, XSSFWorkbook workbook) {

        lastRow++;

        Row actionRow = sheet.createRow(lastRow);
        Cell cell = actionRow.createCell(0, STRING);
        cell.setCellValue(action);

        lastRow += 2;
    }

    private static void createSingleTable(Sheet sheet, JsonData jsonData, XSSFWorkbook workbook) {

        lastRow++;

        // Create header row
        Row headerRow = sheet.createRow(lastRow);
        for (int i = 0; i < jsonData.getHeaders().size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(jsonData.getHeaders().get(i));
        }


        // Create data rows
        for (int i = 0; i < jsonData.getData().size(); i++) {
            Row dataRow = sheet.createRow(lastRow++);
            Map<String, String> rowData = jsonData.getData().get(i);
            for (int j = 0; j < jsonData.getHeaders().size(); j++) {
                String header = jsonData.getHeaders().get(j);
                Cell cell = dataRow.createCell(j);
                cell.setCellValue(rowData.get(header));
            }
        }

        // Auto-size columns
        for (int i = 0; i < jsonData.getHeaders().size(); i++) {
            sheet.autoSizeColumn(i);
        }

        // Create table
        AreaReference tableArea = new AreaReference(new CellReference(lastRow, 0),
                new CellReference(lastRow + 1, jsonData.getHeaders().size() - 1), null);
        XSSFSheet xssfSheet = workbook.getSheetAt(0);
        XSSFTable table = xssfSheet.createTable(tableArea);
        table.setName(jsonData.getName());
        table.setDisplayName(jsonData.getName());

        CTTableStyleInfo styleInfo = table.getCTTable().addNewTableStyleInfo();
        styleInfo.setName("TableStyleMedium2");
        styleInfo.setShowColumnStripes(false);
        styleInfo.setShowRowStripes(true);

        lastRow += 2;

    }

    public static void main(String[] args) {
        String jsonFilePath = "excelFragment3.json";
        String excelFilePath = "output.xlsx";

        try {
            JsonToExcelConverter.convertJsonToExcel(jsonFilePath, excelFilePath);
            System.out.println("JSON data converted to Excel successfully!");
        } catch (IOException e) {
            log.error("Error: {}", e.getMessage());
        }
    }
}