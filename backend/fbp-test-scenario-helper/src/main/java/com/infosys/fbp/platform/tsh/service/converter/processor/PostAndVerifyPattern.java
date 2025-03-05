package com.infosys.fbp.platform.tsh.service.converter.processor;

import com.infosys.fbp.platform.tsh.model.AbstractConcordionHelper;
import com.infosys.fbp.platform.tsh.model.CommandChain;
import com.infosys.fbp.platform.tsh.model.PostAndVerify;
import com.infosys.fbp.platform.tsh.service.converter.Pattern;
import com.infosys.fbp.platform.tsh.util.TDGWorkbook;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import static com.infosys.fbp.platform.tsh.util.TableWithNote.addTableWithNote;

@Slf4j
@AllArgsConstructor
@Getter
public class PostAndVerifyPattern implements Pattern {

    protected PostAndVerify postAndVerify;

    @Override
    public void process(TDGWorkbook workbook, CommandChain commandChain, String commandName) {
        log.info("PostAndVerifyPattern: About to process {}", postAndVerify.getApiName());
        addTableWithNote(workbook, commandChain.getSheetName(), commandChain.getCommands().get(commandName), postAndVerify);
    }

    @Override
    public AbstractConcordionHelper getPattern() {
        return this.postAndVerify;
    }
}
