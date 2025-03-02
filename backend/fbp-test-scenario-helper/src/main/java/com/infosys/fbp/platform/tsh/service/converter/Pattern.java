package com.infosys.fbp.platform.tsh.service.converter;

import com.infosys.fbp.platform.tsh.model.AbstractConcordionHelper;
import com.infosys.fbp.platform.tsh.model.CommandChain;
import com.infosys.fbp.platform.tsh.util.TDGWorkbook;

public interface Pattern {

    void process(TDGWorkbook workbook, CommandChain commandChain, String commandName);

    AbstractConcordionHelper getPattern();
}
