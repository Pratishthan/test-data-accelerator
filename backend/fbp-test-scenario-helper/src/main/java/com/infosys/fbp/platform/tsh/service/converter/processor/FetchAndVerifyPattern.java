package com.infosys.fbp.platform.tsh.service.converter.processor;

import com.infosys.fbp.platform.tsh.model.AbstractConcordionHelper;
import com.infosys.fbp.platform.tsh.model.CommandChain;
import com.infosys.fbp.platform.tsh.model.FetchAndVerify;
import com.infosys.fbp.platform.tsh.service.converter.Pattern;
import com.infosys.fbp.platform.tsh.util.TDGWorkbook;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import static com.infosys.fbp.platform.tsh.util.TableWithNote.addTableWithNote;

@Slf4j
@AllArgsConstructor
@Getter
public class FetchAndVerifyPattern implements Pattern {

    protected FetchAndVerify fetchAndVerify;

    @Override
    public void process(TDGWorkbook workbook, CommandChain commandChain, String commandName) {
        log.info("FetchAndVerifyPattern: About to process {}", fetchAndVerify.getApiName());
        commandChain.getCommands().get(commandName).setCommandName(commandName);
        addTableWithNote(workbook, commandChain.getSheetName(), commandChain.getCommands().get(commandName), fetchAndVerify);
    }

    @Override
    public AbstractConcordionHelper getPattern(){
        return this.fetchAndVerify;
    }
}
