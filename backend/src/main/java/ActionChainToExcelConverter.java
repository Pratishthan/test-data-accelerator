import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import model.Action;
import model.ActionChain;
import model.ApiAction;
import org.apache.commons.lang3.NotImplementedException;
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
public class ActionChainToExcelConverter {

    private static Integer lastRow = 0;


    public static void convertJsonToExcel(String jsonFilePath, String excelFilePath) throws IOException {
        // Read JSON data from file
        ObjectMapper mapper = new ObjectMapper();
        String content = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
        ActionChain actionChain = mapper.readValue(content, ActionChain.class);

        // Create a new workbook and sheet
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");
        workbook.setSheetName(0, "Data");

        actionChain.getData().forEach(
                action -> {
                    switch (action.getType()) {
                        case "Table":
                            createSingleTable(sheet, action, workbook);
                            break;
                        case "APICall":
//                            createActionEntry(sheet, action.getApiAction(), workbook);
                            break;
                        case "Validate":
                            log.debug("Future");
                            break;
                        default:
                            throw new NotImplementedException();
                    }
                }
        );


        // Write workbook to file
        FileOutputStream fileOut = new FileOutputStream("target/" + excelFilePath);
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
    }

    private static void createActionEntry(Sheet sheet, ApiAction action, XSSFWorkbook workbook) {

        lastRow++;

        Row actionRow = sheet.createRow(lastRow);
        Cell cell = actionRow.createCell(0, STRING);
        cell.setCellValue(action.getParams().getFirst().values().toString());

        lastRow += 2;
    }

    private static void createSingleTable(Sheet sheet, Action tableAction, XSSFWorkbook workbook) {

        lastRow++;

        Integer startRow = lastRow; //Integer.valueOf(lastRow.intValue());


        // Create header row
        Row headerRow = sheet.createRow(lastRow++);
        for (int i = 0; i < tableAction.getTableAction().getHeaders().size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(tableAction.getTableAction().getHeaders().get(i));
        }


        // Create data rows
        for (int i = 0; i < tableAction.getTableAction().getData().size(); i++) {
            Row dataRow = sheet.createRow(lastRow++);
            Map<String, String> rowData = tableAction.getTableAction().getData().get(i);
            for (int j = 0; j < tableAction.getTableAction().getHeaders().size(); j++) {
                String header = tableAction.getTableAction().getHeaders().get(j);
                Cell cell = dataRow.createCell(j);
                cell.setCellValue(rowData.get(header));
            }
        }

        // Auto-size columns
        for (int i = 0; i < tableAction.getTableAction().getHeaders().size(); i++) {
            sheet.autoSizeColumn(i);
        }

        // Create table
        System.out.println("Start {}, End {} & {}" + startRow + (lastRow) +  tableAction.getTableAction().getHeaders().size());
        AreaReference tableArea = new AreaReference(new CellReference(startRow, 0),
                new CellReference(lastRow - 1, tableAction.getTableAction().getHeaders().size() - 1), null);
        XSSFSheet xssfSheet = workbook.getSheetAt(0);
        XSSFTable table = xssfSheet.createTable(tableArea);
        table.setName(tableAction.getName());
        table.setDisplayName(tableAction.getName());

        CTTableStyleInfo styleInfo = table.getCTTable().addNewTableStyleInfo();
        styleInfo.setName("TableStyleMedium2");
        styleInfo.setShowColumnStripes(false);
        styleInfo.setShowRowStripes(true);

        lastRow += 2;

    }

    public static void main(String[] args) {
        String jsonFilePath = "excelFragment.json";
        String excelFilePath = "output.xlsx";

        try {
            ActionChainToExcelConverter.convertJsonToExcel(jsonFilePath, excelFilePath);
            System.out.println("JSON data converted to Excel successfully!");
        } catch (IOException e) {
            log.error("Error: {}", e.getMessage());
        }
    }
}