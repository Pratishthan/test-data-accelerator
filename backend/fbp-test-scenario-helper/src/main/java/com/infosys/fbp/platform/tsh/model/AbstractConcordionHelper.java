package com.infosys.fbp.platform.tsh.model;

import lombok.Data;

@Data
public class AbstractConcordionHelper implements ConcordionHelper {

    private String componentName;
    private String actionCodeGroupName;

    @Override
    public String getConcordionCommand() {
        return "";
    }
}
