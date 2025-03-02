package com.infosys.fbp.platform.tsh.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Parameter implements ConcordionHelper {

    private String name;
    private String displayText;
    private String defaultValue;

    @Override
    public String getConcordionCommand() {
        return "concordion:set=\"#" + name + "\"";
    }
}
