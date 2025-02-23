package com.pratishthanventures.tdg.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.map.SingletonMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SetAndExecute implements ConcordionHelper {

    private String functionName;
    private String displayText;
    private List<Map<String, String>> parameterList; // Parameter Name and Default Value
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
            sb.append(getParameterConcordionCommand().get(0));
            sb.append("\n");
            sb.append("../");
        }
        sb.append("concordion:execute=\"").append(functionName).append("(");

        for (int i = 0; i < parameterList.size(); i++) {
            SingletonMap<String, String> parameter = new SingletonMap<>(parameterList.get(i));
            sb.append("#").append(parameter.getKey());
            if (i != parameterList.size() - 1) {
                sb.append(",");
            }
        }
        sb.append(")\"");
        return sb.toString();
    }

    public List<String> getParameterConcordionCommand() {
        List<String> commandList = new ArrayList<>();
        for (Map<String, String> parameter : parameterList) {
            SingletonMap<String, String> parameterMap = new SingletonMap<>(parameter);
            commandList.add("concordion:set=\"#" + parameterMap.getKey() + "\"");
        }
        return commandList;
    }


}
