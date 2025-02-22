package com.pratishthanventures.tdg.output;

import com.pratishthanventures.tdg.model.TableMapperHelper;

import static com.pratishthanventures.tdg.output.TableWithNote.addTableWithNote;

public class TableMapperImpl implements TableMapper {
    @Override
    public void addMapperToSheet(TDGWorkbook workbook, String sheetName, TableMapperHelper tableMapperHelper) {
        addTableWithNote(workbook, sheetName, tableMapperHelper);
    }
}
