package com.pratishthanventures.tdg.service.converter.processor;

import com.pratishthanventures.tdg.model.SetAndExecute;
import com.pratishthanventures.tdg.service.converter.Pattern;
import com.pratishthanventures.tdg.util.TDGWorkbook;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.SingletonMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import static com.pratishthanventures.tdg.util.Note.addNoteToCell;

@Slf4j
@AllArgsConstructor
public class SetAndExecutePattern implements Pattern {

    protected SetAndExecute setAndExecute;

    @Override
    public void process(TDGWorkbook workbook, String sheetName) {
        log.info("SimpleCommandPattern: About to process {}", setAndExecute.getFunctionName());
        if ("true".equalsIgnoreCase(setAndExecute.getIsCombined())) {
            Row row = workbook.getNewRow(sheetName);
            Cell textCell = row.createCell(1);
            textCell.setCellValue(setAndExecute.getDisplayText());
            Cell valueCell = row.createCell(2);
            valueCell.setCellValue(setAndExecute.getFunctionName());
            addNoteToCell(workbook, sheetName, valueCell, setAndExecute.getConcordionCommand());
        } else {
            for (int i = 0; i < setAndExecute.getParameterList().size(); i++) {
                SingletonMap<String, String> parameter = new SingletonMap<>(setAndExecute.getParameterList().get(i));
                Row row = workbook.getNewRow(sheetName);
                Cell textCell = row.createCell(1);
                textCell.setCellValue(parameter.getKey());
                Cell valueCell = row.createCell(2);
                valueCell.setCellValue(parameter.getValue());
                addNoteToCell(workbook, sheetName, valueCell, setAndExecute.getParameterConcordionCommand().get(i));
            }

            Row callerRow = workbook.getNewRow(sheetName);
            Cell valueCell = callerRow.createCell(1);
            valueCell.setCellValue(setAndExecute.getDisplayText());
            addNoteToCell(workbook, sheetName, valueCell, setAndExecute.getConcordionCommand());
        }
    }
}
