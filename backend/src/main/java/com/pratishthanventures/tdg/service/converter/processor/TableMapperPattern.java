package com.pratishthanventures.tdg.service.converter.processor;

import com.pratishthanventures.tdg.model.TableMapper;
import com.pratishthanventures.tdg.output.TDGWorkbook;
import com.pratishthanventures.tdg.service.converter.Pattern;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.pratishthanventures.tdg.output.TableWithNote.addTableWithNote;

@Slf4j
@AllArgsConstructor
public class TableMapperPattern implements Pattern {

    protected TableMapper tableMapper;

    @Override
    public void process(TDGWorkbook workbook, String sheetName) {
        log.info("TableMapperPattern: About to process {}", tableMapper.getTableName());
        addTableWithNote(workbook, sheetName, tableMapper);
    }
}
