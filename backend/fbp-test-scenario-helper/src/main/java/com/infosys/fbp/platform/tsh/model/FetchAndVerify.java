package com.infosys.fbp.platform.tsh.model;

import com.infosys.fbp.platform.tsh.PropertyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FetchAndVerify extends AbstractConcordionHelper {
    private String apiName;
    private EnumMap<PropertyType, List<Property>> propertyListMap;


    public String getConcordionCommand() {
        return "(table)concordion:execute=\"#result=fetchResultList('" + apiName + "',#ROW)\"";
    }

    private String getVerifyCommand() {
        return """
                (table)concordion:verify-rows="#result : resultList"
                concordion:assertEquals="#result.""" + propertyListMap.get(PropertyType.ResponseBodyColumnList).stream().toList().get(0).getTechnicalColumnName() + "\"";
    }

    public String getPostCommand() {
        return "(table)concordion:execute=\"#result=callMapper('" + apiName + "', #ROW)\"";
    }

    private String getAssertCommand(Property resultColumn) {
        return "concordion:assertEquals=\"#result." + resultColumn.getTechnicalColumnName() + "\"";
    }

    public List<String> getVerifyCommands() {
        List<String> commands = new ArrayList<>();
        commands.add(getVerifyCommand());
        commands.addAll(propertyListMap.get(PropertyType.ResponseBodyColumnList).stream().skip(1).map(this::getAssertCommand).toList());
        return commands;
    }

    public Map<String, String> getColumnMapForRequest() {
        Map<String, String> columnWithCommandMap = new LinkedHashMap<>();
        if (!propertyListMap.get(PropertyType.RequestBodyColumnList).isEmpty()) {
            Property idxProperty = propertyListMap.get(PropertyType.RequestBodyColumnList).get(0);
            columnWithCommandMap.put(idxProperty.getBusinessColumnName(), getPostCommand());
            propertyListMap.get(PropertyType.RequestBodyColumnList).stream().skip(1).forEach(property ->
                    columnWithCommandMap.putIfAbsent(property.getBusinessColumnName(), ""));
        }
        return columnWithCommandMap;
    }

    public Map<String, String> getColumnMapForVerify() {
        Map<String, String> columnWithCommandMap = new LinkedHashMap<>();
        if (!propertyListMap.get(PropertyType.ResponseBodyColumnList).isEmpty()) {
            Property idxProperty = propertyListMap.get(PropertyType.ResponseBodyColumnList).get(0);
            columnWithCommandMap.put(idxProperty.getBusinessColumnName(), getVerifyCommand());
            propertyListMap.get(PropertyType.ResponseBodyColumnList).stream().skip(1).forEach(property ->
                    columnWithCommandMap.putIfAbsent(property.getBusinessColumnName(), getAssertCommand(property)));
        }
        return columnWithCommandMap;
    }

}
