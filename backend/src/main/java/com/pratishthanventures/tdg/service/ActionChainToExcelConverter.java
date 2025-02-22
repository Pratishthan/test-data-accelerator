package com.pratishthanventures.tdg.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pratishthanventures.tdg.dto.TableHeader;
import com.pratishthanventures.tdg.util.TDGWorkbook;
import lombok.extern.slf4j.Slf4j;
import com.pratishthanventures.tdg.dto.Action;
import com.pratishthanventures.tdg.dto.ActionChain;
import com.pratishthanventures.tdg.dto.ApiAction;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyleInfo;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.apache.poi.ss.usermodel.CellType.STRING;

@Slf4j
public class ActionChainToExcelConverter {

    private static Integer lastRow = 0;


    public static void convertJsonToExcel(String jsonFilePath, String excelFilePath) throws IOException {
        ActionChain actionChain = getActionChainFromFile(jsonFilePath);

        // Create a new workbook and sheet
//        XSSFWorkbook workbook = new XSSFWorkbook();
//        Sheet sheet = workbook.createSheet("Data");
//        workbook.setSheetName(0, "Data");

        TDGWorkbook workbook = new TDGWorkbook("Data");
        Sheet sheet = workbook.getWorkbook().getSheet("Data");

        actionChain.getData().forEach(
                action -> {
                    switch (action.getType()) {
                        case "Table":
                            createSingleTable(sheet, action, workbook);
                            break;
                        case "APICall":
                            createActionEntry(sheet, action.getApiAction(), workbook);
                            break;
                        case "Validate":
                            log.debug("Future");
                            break;
                        default:
                            throw new NotImplementedException();
                    }
                }
        );

        workbook.writeWorkbookToFile("target/" + excelFilePath);
    }

    private static ActionChain getActionChainFromFile(String jsonFilePath) throws IOException {
        // Read JSON data from file
        String content;
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream inputStream = ActionChainToExcelConverter.class.getClassLoader().getResourceAsStream(jsonFilePath)) {
            if (inputStream == null) {
                throw new IOException("Resource file not found: " + jsonFilePath);
            }
            content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("Error reading resource file: {}", jsonFilePath, e);
            throw e;
        }
        return mapper.readValue(content, ActionChain.class);
    }

    private static void createActionEntry(Sheet sheet, ApiAction action, TDGWorkbook workbook) {

        lastRow++;

        Row actionRow = sheet.createRow(lastRow);
        Cell cell = actionRow.createCell(0, STRING);
        cell.setCellValue(action.getParams().get(0).values().toString());

        lastRow += 2;
    }

    private static void createSingleTable(Sheet sheet, Action tableAction, TDGWorkbook workbook) {

        // Empty row above
        lastRow++;

        Integer startRow = lastRow;


        // Create header row
        Row headerRow = sheet.createRow(lastRow++);
        for (int i = 0; i < tableAction.getTableAction().getHeaders().size(); i++) {
            TableHeader tableHeader = tableAction.getTableAction().getHeaders().get(i);
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(tableHeader.getName());
            // Add note to cell if needed
            if (StringUtils.isNotBlank(tableHeader.getSanityAction())) {
                addNoteToCell(sheet, cell, tableHeader.getSanityAction());
            }
        }


        // Create data rows
        for (int i = 0; i < tableAction.getTableAction().getData().size(); i++) {
            Row dataRow = sheet.createRow(lastRow++);
            Map<String, String> rowData = tableAction.getTableAction().getData().get(i);
            for (int j = 0; j < tableAction.getTableAction().getHeaders().size(); j++) {
                String header = tableAction.getTableAction().getHeaders().get(j).getName();
                Cell cell = dataRow.createCell(j);
                cell.setCellValue(rowData.get(header));
            }
        }

        // Auto-size columns
        for (int i = 0; i < tableAction.getTableAction().getHeaders().size(); i++) {
            sheet.autoSizeColumn(i);
        }

        // Create table
        log.debug("Start {}, End {} & {}", startRow, lastRow, tableAction.getTableAction().getHeaders().size());
        AreaReference tableArea = new AreaReference(new CellReference(startRow, 0),
                new CellReference(lastRow - 1, tableAction.getTableAction().getHeaders().size() - 1), null);
        XSSFSheet xssfSheet = workbook.getWorkbook().getSheetAt(0);
        XSSFTable table = xssfSheet.createTable(tableArea);
        table.setName(tableAction.getName());

        CTTableStyleInfo styleInfo = table.getCTTable().addNewTableStyleInfo();
        styleInfo.setName("TableStyleMedium2");
        styleInfo.setShowColumnStripes(false);
        styleInfo.setShowRowStripes(true);

        lastRow += 2;

    }

    private static void addNoteToCell(Sheet sheet, Cell cell, String noteText) {
        Drawing<?> drawing = sheet.createDrawingPatriarch();
        ClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0,
                cell.getColumnIndex(), cell.getRowIndex(), cell.getColumnIndex() + 2, cell.getRowIndex() + 3);

        Comment comment = drawing.createCellComment(anchor);
        comment.setString(new XSSFRichTextString(noteText));
        cell.setCellComment(comment);

    }

    public static void main(String[] args) {
        String jsonFilePath = "excelFragment.json";
        String excelFilePath = "output.xlsx";

        try {
            ActionChainToExcelConverter.convertJsonToExcel(jsonFilePath, excelFilePath);
            log.info("JSON data converted to Excel successfully!");
        } catch (IOException e) {
            log.error("Error: {}", e.getMessage());
        }
    }
}