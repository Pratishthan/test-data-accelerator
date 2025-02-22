package com.pratishthanventures.tdg.service.converter;

import com.pratishthanventures.tdg.output.TDGWorkbook;

public interface Pattern {

    void process(TDGWorkbook workbook, String sheetName);
}
