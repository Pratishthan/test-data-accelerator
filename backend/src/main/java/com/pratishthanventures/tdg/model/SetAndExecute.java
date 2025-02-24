package com.pratishthanventures.tdg.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SetAndExecute extends AbstractConcordionHelper {

    private String functionName;
    private String displayText;
    private List<Parameter> parameterList; // Parameter Name and Default Value
    private String isCombined; // true or false --> true would imply only 1 Parameter is passed and it is combined with the command

    public String getConcordionCommand() {
        /*
          concordion:set="#bodDate"
          concordion:execute="setBODDate(#bodDate)"
          OR (Single Cell)
          "concordion:set=""#bodDate2""
          ../concordion:execute=""setBODDate(#bodDate2)"""
         */

        StringBuilder sb = new StringBuilder();
        if (isCombined.equals("true")) {
            sb.append(parameterList.get(0).getConcordionCommand());
            sb.append("\n");
            sb.append("../");
        }
        sb.append("concordion:execute=\"").append(functionName).append("(");

        for (int i = 0; i < parameterList.size(); i++) {
            Parameter parameter = parameterList.get(i);
            sb.append("#").append(parameter.getName());
            if (i != parameterList.size() - 1) {
                sb.append(",");
            }
        }
        sb.append(")\"");
        return sb.toString();
    }

}
