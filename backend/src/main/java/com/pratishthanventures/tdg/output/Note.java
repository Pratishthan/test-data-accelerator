package com.pratishthanventures.tdg.output;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

public class Note {

    public static void addNoteToCell(Sheet sheet, Cell cell, String noteText) {
        Drawing<?> drawing = sheet.createDrawingPatriarch();
        ClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0,
                cell.getColumnIndex(), cell.getRowIndex(), cell.getColumnIndex() + 2, cell.getRowIndex() + 3);

        Comment comment = drawing.createCellComment(anchor);
        comment.setString(new XSSFRichTextString(noteText));
        cell.setCellComment(comment);
    }
}
