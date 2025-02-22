package com.pratishthanventures.tdg.service.converter.strategy;

import com.pratishthanventures.tdg.model.SimpleCommandHelper;
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

    protected SimpleCommandHelper simpleCommandHelper;

    @Override
    public void process(TDGWorkbook workbook, String sheetName) {
        log.info("SimpleCommandPattern: About to process {}", simpleCommandHelper.getText());
        Row row = workbook.getNewRow(sheetName);
        Cell cell = row.createCell(1);
        cell.setCellValue(simpleCommandHelper.getText());

        Note.addNoteToCell(workbook, sheetName, cell, simpleCommandHelper.getConcordionCommand());
    }
}
