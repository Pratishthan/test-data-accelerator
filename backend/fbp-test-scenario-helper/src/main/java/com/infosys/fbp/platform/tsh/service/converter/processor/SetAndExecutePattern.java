package com.infosys.fbp.platform.tsh.service.converter.processor;

import com.infosys.fbp.platform.tsh.model.AbstractConcordionHelper;
import com.infosys.fbp.platform.tsh.model.CommandChain;
import com.infosys.fbp.platform.tsh.model.Parameter;
import com.infosys.fbp.platform.tsh.model.SetAndExecute;
import com.infosys.fbp.platform.tsh.service.converter.Pattern;
import com.infosys.fbp.platform.tsh.util.TDGWorkbook;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import static com.infosys.fbp.platform.tsh.util.Note.addNoteToCell;
import static com.infosys.fbp.platform.tsh.util.TDGWorkbook.getSeparatorRow;

@Slf4j
@AllArgsConstructor
public class SetAndExecutePattern implements Pattern {

    protected SetAndExecute setAndExecute;

    @Override
    public void process(TDGWorkbook workbook, CommandChain commandChain, String commandName) {
        String sheetName = commandChain.getSheetName();
        log.info("SimpleCommandPattern: About to process {}", setAndExecute.getFunctionName());
        getSeparatorRow(workbook, sheetName, commandName);
        if ("true".equalsIgnoreCase(setAndExecute.getIsCombined())) {
            Row row = workbook.getNewRow(sheetName);
            Cell textCell = row.createCell(1);
            textCell.setCellValue(setAndExecute.getDisplayText());
            Cell valueCell = row.createCell(2);
            valueCell.setCellValue(setAndExecute.getFunctionName());
            addNoteToCell(workbook, sheetName, valueCell, setAndExecute.getConcordionCommand());
        } else {
            for (int i = 0; i < setAndExecute.getParameterList().size(); i++) {
                Parameter parameter = setAndExecute.getParameterList().get(i);
                Row row = workbook.getNewRow(sheetName);
                Cell textCell = row.createCell(1);
                textCell.setCellValue(parameter.getDisplayText());
                Cell valueCell = row.createCell(2);
                valueCell.setCellValue(parameter.getDefaultValue());
                addNoteToCell(workbook, sheetName, valueCell, parameter.getConcordionCommand());
            }

            Row callerRow = workbook.getNewRow(sheetName);
            Cell valueCell = callerRow.createCell(1);
            valueCell.setCellValue(setAndExecute.getDisplayText());
            addNoteToCell(workbook, sheetName, valueCell, setAndExecute.getConcordionCommand());
        }
    }

    @Override
    public AbstractConcordionHelper getPattern(){
        return this.setAndExecute;
    }
}
