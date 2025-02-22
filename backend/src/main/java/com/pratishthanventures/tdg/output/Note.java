package com.pratishthanventures.tdg.output;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

import static com.pratishthanventures.tdg.Constants.GAP_BETWEEN_COMMANDS;

public class Note {

    public static void addNoteToCell(TDGWorkbook workbook, String sheetName, Cell cell, String noteText) {
        Sheet sheet = workbook.getWorkbook().getSheet(sheetName);
        Drawing<?> drawing = sheet.createDrawingPatriarch();
        ClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0,
                cell.getColumnIndex(), cell.getRowIndex(), cell.getColumnIndex() + 2, cell.getRowIndex() + 3);

        Comment comment = drawing.createCellComment(anchor);
        comment.setString(new XSSFRichTextString(noteText));
        cell.setCellComment(comment);

        workbook.getSheetLastRow().replace(sheetName, workbook.getSheetLastRow().get(sheetName) + GAP_BETWEEN_COMMANDS);
    }
}
