package com.infosys.fbp.platform.tsh.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FetchAndVerify extends AbstractConcordionHelper {
    private String apiName;
    private List<String> parameterList;
    private Map<String, Property> resultColumnMap;

    public String getConcordionCommand() {
        return "(table)concordion:execute=\"#result=fetchResultList('" + apiName + "',#ROW)\"";
    }

    private String getVerifyCommand() {
        return """
                (table)concordion:verify-rows="#result : resultList"
                concordion:assertEquals="#result.""" + resultColumnMap.keySet().stream().toList().get(0) + "\"";
    }

    private String getAssertCommand(Property resultColumn) {
        return "concordion:assertEquals=\"#result." + resultColumn.getTechnicalColumnName() + "\"";
    }

    public List<String> getVerifyCommands() {
        List<String> commands = new ArrayList<>();
        commands.add(getVerifyCommand());
        commands.addAll(resultColumnMap.entrySet().stream().skip(1).map(x -> getAssertCommand(x.getValue())).toList());
        return commands;
    }

}
