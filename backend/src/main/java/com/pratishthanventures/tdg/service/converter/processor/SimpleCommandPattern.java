package com.pratishthanventures.tdg.service.converter.processor;

import com.pratishthanventures.tdg.model.SimpleCommand;
import com.pratishthanventures.tdg.output.Note;
import com.pratishthanventures.tdg.output.TDGWorkbook;
import com.pratishthanventures.tdg.service.converter.Pattern;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

@Slf4j
@AllArgsConstructor
public class SimpleCommandPattern implements Pattern {

    protected SimpleCommand simpleCommand;

    @Override
    public void process(TDGWorkbook workbook, String sheetName) {
        log.info("SimpleCommandPattern: About to process {}", simpleCommand.getText());
        Row row = workbook.getNewRow(sheetName);
        Cell cell = row.createCell(1);
        cell.setCellValue(simpleCommand.getText());

        Note.addNoteToCell(workbook, sheetName, cell, simpleCommand.getConcordionCommand());
    }
}
