package com.infosys.fbp.platform.tsh.service.converter.processor;

import com.infosys.fbp.platform.tsh.model.AbstractConcordionHelper;
import com.infosys.fbp.platform.tsh.model.CommandChain;
import com.infosys.fbp.platform.tsh.model.SimpleCommand;
import com.infosys.fbp.platform.tsh.service.converter.Pattern;
import com.infosys.fbp.platform.tsh.util.Note;
import com.infosys.fbp.platform.tsh.util.TDGWorkbook;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import static com.infosys.fbp.platform.tsh.util.TDGWorkbook.getSeparatorRow;

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

    @Override
    public AbstractConcordionHelper getPattern(){
        return this.simpleCommand;
    }
}
