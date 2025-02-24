package com.pratishthanventures.tdg.service.converter.processor;

import com.pratishthanventures.tdg.model.CommandChain;
import com.pratishthanventures.tdg.model.SimpleCommand;
import com.pratishthanventures.tdg.service.converter.Pattern;
import com.pratishthanventures.tdg.util.Note;
import com.pratishthanventures.tdg.util.TDGWorkbook;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import static com.pratishthanventures.tdg.util.TDGWorkbook.getSeparatorRow;

@Slf4j
@AllArgsConstructor
public class SimpleCommandPattern implements Pattern {

    protected SimpleCommand simpleCommand;

    @Override
    public void process(TDGWorkbook workbook, CommandChain commandChain, String commandName) {
        String sheetName = commandChain.getSheetName();
        log.info("SimpleCommandPattern: About to process {}", simpleCommand.getText());
        getSeparatorRow(workbook, sheetName, commandName);
        Row row = workbook.getNewRow(sheetName);
        Cell cell = row.createCell(1);
        cell.setCellValue(simpleCommand.getText());

        Note.addNoteToCell(workbook, sheetName, cell, simpleCommand.getConcordionCommand());
    }
}
