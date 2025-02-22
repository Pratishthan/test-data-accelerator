package com.pratishthanventures.tdg.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SimpleCommandHelper implements ConcordionHelper {
    private String functionName;
    private String text;

    public String getConcordionCommand() {
        return "concordion:execute=\"" + this.functionName + "()\"";
    }

}
