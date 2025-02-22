package com.pratishthanventures.tdg.service.converter.processor;

import com.pratishthanventures.tdg.model.FetchAndVerify;
import com.pratishthanventures.tdg.service.converter.Pattern;
import com.pratishthanventures.tdg.util.TDGWorkbook;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.pratishthanventures.tdg.util.TableWithNote.addTableWithNote;

@Slf4j
@AllArgsConstructor
public class FetchAndVerifyPattern implements Pattern {

    protected FetchAndVerify fetchAndVerify;

    @Override
    public void process(TDGWorkbook workbook, String sheetName) {
        log.info("FetchAndVerifyPattern: About to process {}", fetchAndVerify.getApiName());
        addTableWithNote(workbook, sheetName, fetchAndVerify);
    }
}
