package com.pratishthanventures.tdg.output;

import com.pratishthanventures.tdg.model.SimpleCommandHelper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class SimpleCommandImpl implements SimpleCommand {

    @Override
    public void addCommandToSheet(Sheet sheet, Integer rowNumber, SimpleCommandHelper simpleCommandHelper) {

        Row row = sheet.createRow(rowNumber);
        Cell cell = row.createCell(1);
        cell.setCellValue(simpleCommandHelper.getText());

        Note.addNoteToCell(sheet, cell, simpleCommandHelper.getConcordionCommand());


    }
}
