package com.pratishthanventures.tdg.service.converter.strategy;

import com.pratishthanventures.tdg.model.TableMapperHelper;
import com.pratishthanventures.tdg.output.TDGWorkbook;
import com.pratishthanventures.tdg.service.converter.Pattern;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.pratishthanventures.tdg.output.TableWithNote.addTableWithNote;

@Slf4j
@NoArgsConstructor
public class TableMapperPattern implements Pattern {

    protected TableMapperHelper tableMapperHelper;

    @Override
    public void process(TDGWorkbook workbook, String sheetName) {
        log.info("About to process {}", tableMapperHelper.getTableName());
        addTableWithNote(workbook, sheetName, tableMapperHelper);
    }
}
