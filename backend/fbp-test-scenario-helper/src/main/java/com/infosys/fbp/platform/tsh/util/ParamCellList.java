package com.infosys.fbp.platform.tsh.util;

import com.infosys.fbp.platform.tsh.model.Parameter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.List;

import static com.infosys.fbp.platform.tsh.util.Note.addNoteToCell;

public class ParamCellList {

    public static void addParamCellList(TDGWorkbook workbook, String sheetName, List<Parameter> parameterList) {
        for (Parameter parameter : parameterList) {
            Row row = workbook.getNewRow(sheetName);
            Cell textCell = row.createCell(1);
            textCell.setCellValue(parameter.getDisplayText());
            Cell valueCell = row.createCell(2);
            valueCell.setCellValue(parameter.getDefaultValue());
            valueCell.setCellStyle(workbook.createBlueStyle());
            addNoteToCell(workbook, sheetName, valueCell, parameter.getConcordionCommand());
        }
    }
}
