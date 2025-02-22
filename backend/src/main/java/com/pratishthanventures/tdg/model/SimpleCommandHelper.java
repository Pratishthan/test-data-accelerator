package com.pratishthanventures.tdg.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleCommandHelper implements ConcordionHelper {
    private String functionName;
    private String text;

    public String getConcordionCommand() {
        return "concordion:execute=\"" + this.functionName + "()\"";
    }

}
