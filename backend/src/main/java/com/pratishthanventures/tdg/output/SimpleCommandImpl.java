package com.pratishthanventures.tdg.output;

import com.pratishthanventures.tdg.model.SimpleCommandHelper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class SimpleCommandImpl implements SimpleCommand {

    @Override
    public void addCommandToSheet(TDGWorkbook workbook, String sheetName, SimpleCommandHelper simpleCommandHelper) {

        Row row = workbook.getNewRow(sheetName);
        Cell cell = row.createCell(1);
        cell.setCellValue(simpleCommandHelper.getText());

        Note.addNoteToCell(workbook, sheetName, cell, simpleCommandHelper.getConcordionCommand());


    }
}
