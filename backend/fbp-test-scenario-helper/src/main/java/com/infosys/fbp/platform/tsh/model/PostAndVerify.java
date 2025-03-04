package com.infosys.fbp.platform.tsh.model;

import com.infosys.fbp.platform.tsh.PropertyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostAndVerify extends AbstractConcordionHelper {
    private String apiName;
    private EnumMap<PropertyType, List<Property>> propertyListMap;

    public String getConcordionCommand() {
        return "(table)concordion:execute=\"#result=callPostApi('" + apiName + "',#ROW)\"";
    }

    private String getVerifyCommand() {
        return """
                (table)concordion:verify-rows="#result : resultList"
                concordion:assertEquals="#result.""" + propertyListMap.get(PropertyType.RequestBodyColumnList).get(0) + "\"";
    }

    private String getAssertCommand(Property resultColumn) {
        return "concordion:assertEquals=\"#result." + resultColumn.getTechnicalColumnName() + "\"";
    }

    public List<String> getVerifyCommands() {
        List<String> commands = new ArrayList<>();
        commands.add(getVerifyCommand());
        commands.addAll(propertyListMap.get(PropertyType.RequestBodyColumnList).stream().skip(1).map(this::getAssertCommand).toList());
        return commands;
    }

}
