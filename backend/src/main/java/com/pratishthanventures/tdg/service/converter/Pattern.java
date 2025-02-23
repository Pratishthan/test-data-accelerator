package com.pratishthanventures.tdg.service.converter;

import com.pratishthanventures.tdg.util.TDGWorkbook;

import java.util.List;
import java.util.Map;

public interface Pattern {

    void process(TDGWorkbook workbook, String sheetName, List<String> columnNames, List<Map<String, String>> data);
}
