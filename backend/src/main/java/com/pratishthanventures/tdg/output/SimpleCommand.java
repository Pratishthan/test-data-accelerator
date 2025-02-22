package com.pratishthanventures.tdg.output;

import com.pratishthanventures.tdg.model.SimpleCommandHelper;

public interface SimpleCommand {

    void addCommandToSheet(TDGWorkbook workbook, String sheetName, SimpleCommandHelper simpleCommandHelper);
}
