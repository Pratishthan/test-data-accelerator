package com.pratishthanventures.tdg.service.converter;

import com.pratishthanventures.tdg.model.CommandChain;
import com.pratishthanventures.tdg.util.TDGWorkbook;

public interface Pattern {

    void process(TDGWorkbook workbook, CommandChain commandChain, String commandName);
}
