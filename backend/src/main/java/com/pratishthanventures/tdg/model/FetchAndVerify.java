package com.pratishthanventures.tdg.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FetchAndVerify extends AbstractConcordionHelper {
    private String apiName;
    private List<String> parameterList;
    private List<String> resultColumnList;

    public String getConcordionCommand() {
        return "(table)concordion:execute=\"#result=fetchResultList('" + apiName + "',#ROW)\"";
    }

    private String getVerifyCommand() {
        return """
                (table)concordion:verify-rows="#result : resultList"
                concordion:assertEquals="#result.""" + resultColumnList.get(0) + "\"";
    }

    private String getAssertCommand(String resultColumn) {
        return "concordion:assertEquals=\"#result." + resultColumn + "\"";
    }

    public List<String> getVerifyCommands() {
        List<String> commands = new ArrayList<>();
        commands.add(getVerifyCommand());
        commands.addAll(resultColumnList.stream().skip(1).map(this::getAssertCommand).toList());
        return commands;
    }

}
