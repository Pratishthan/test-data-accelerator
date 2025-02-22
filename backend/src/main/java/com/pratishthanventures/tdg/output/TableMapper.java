package com.pratishthanventures.tdg.output;

import com.pratishthanventures.tdg.model.TableMapperHelper;

public interface TableMapper {

    void addMapperToSheet(TDGWorkbook workbook, String sheetName, TableMapperHelper tableMapperHelper);
}
