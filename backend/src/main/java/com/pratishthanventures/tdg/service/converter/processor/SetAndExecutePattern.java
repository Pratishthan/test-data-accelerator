package com.pratishthanventures.tdg.service.converter.processor;

import com.pratishthanventures.tdg.model.Parameter;
import com.pratishthanventures.tdg.model.SetAndExecute;
import com.pratishthanventures.tdg.service.converter.Pattern;
import com.pratishthanventures.tdg.util.TDGWorkbook;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.List;
import java.util.Map;

import static com.pratishthanventures.tdg.util.Note.addNoteToCell;
import static com.pratishthanventures.tdg.util.TDGWorkbook.getSeparatorRow;

@Slf4j
@AllArgsConstructor
public class SetAndExecutePattern implements Pattern {

    protected SetAndExecute setAndExecute;

    @Override
    public void process(TDGWorkbook workbook, String sheetName, List<String> columnNames, List<Map<String, String>> data) {
        log.info("SimpleCommandPattern: About to process {}", setAndExecute.getFunctionName());
        getSeparatorRow(workbook, sheetName);
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
}
