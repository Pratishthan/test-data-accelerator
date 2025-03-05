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
    private EnumMap<PropertyType, List<Property>> typeDenormPropertiesMap;
    private EnumMap<PropertyType, Map<String, List<Property>>> typeNormalPropertyMap;


    public String getConcordionCommand() {
        return "(table)concordion:execute=\"#result=fetchResultList('" + apiName + "',#ROW)\"";
    }

    private String getVerifyCommand() {
        return """
                (table)concordion:verify-rows="#result : resultList"
                concordion:assertEquals="#result.""" + typeDenormPropertiesMap.get(PropertyType.ResponseBodyColumnList).stream().toList().get(0).getTechnicalColumnName() + "\"";
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
        commands.addAll(typeDenormPropertiesMap.get(PropertyType.ResponseBodyColumnList).stream().skip(1).map(this::getAssertCommand).toList());
        return commands;
    }

    public Map<String, String> getDenormColumnMapForRequest() {
        Map<String, String> columnWithCommandMap = new LinkedHashMap<>();
        if (!typeDenormPropertiesMap.get(PropertyType.RequestBodyColumnList).isEmpty()) {
            Property idxProperty = typeDenormPropertiesMap.get(PropertyType.RequestBodyColumnList).get(0);
            columnWithCommandMap.put(idxProperty.getBusinessColumnName(), getPostCommand());
            typeDenormPropertiesMap.get(PropertyType.RequestBodyColumnList).stream().skip(1).forEach(property ->
                    columnWithCommandMap.putIfAbsent(property.getBusinessColumnName(), ""));
        }
        return columnWithCommandMap;
    }

    public Map<String, Map<String, String>> getNormalColumnMapForRequest() {
        Map<String, Map<String, String>> columnWithCommandMap = new LinkedHashMap<>();
        typeNormalPropertyMap.get(PropertyType.RequestBodyColumnList).forEach( (schema, properties) ->  {
            Map<String, String> schemaColumnWithCommandMap = new LinkedHashMap<>();

            Property idxProperty = properties.get(0);
            schemaColumnWithCommandMap.put(idxProperty.getBusinessColumnName(), getPostCommand());
            properties.stream().skip(1).forEach(property ->
                    schemaColumnWithCommandMap.putIfAbsent(property.getBusinessColumnName(), ""));

            columnWithCommandMap.put(schema, schemaColumnWithCommandMap);
        });
        return columnWithCommandMap;
    }

    public Map<String, String> getDenormColumnMapForVerify() {
        Map<String, String> columnWithCommandMap = new LinkedHashMap<>();
        if (!typeDenormPropertiesMap.get(PropertyType.ResponseBodyColumnList).isEmpty()) {
            Property idxProperty = typeDenormPropertiesMap.get(PropertyType.ResponseBodyColumnList).get(0);
            columnWithCommandMap.put(idxProperty.getBusinessColumnName(), getVerifyCommand());
            typeDenormPropertiesMap.get(PropertyType.ResponseBodyColumnList).stream().skip(1).forEach(property ->
                    columnWithCommandMap.putIfAbsent(property.getBusinessColumnName(), getAssertCommand(property)));
        }
        return columnWithCommandMap;
    }

    public Map<String, Map<String, String>> getNormalColumnMapForVerify() {
        Map<String, Map<String, String>> columnWithCommandMap = new LinkedHashMap<>();
        typeNormalPropertyMap.get(PropertyType.ResponseBodyColumnList).forEach( (schema, properties) ->  {
            Map<String, String> schemaColumnWithCommandMap = new LinkedHashMap<>();

            Property idxProperty = properties.get(0);
            schemaColumnWithCommandMap.put(idxProperty.getBusinessColumnName(), getVerifyCommand());
            properties.stream().skip(1).forEach(property ->
                    schemaColumnWithCommandMap.putIfAbsent(property.getBusinessColumnName(), getAssertCommand(property)));

            columnWithCommandMap.put(schema, schemaColumnWithCommandMap);
        });
        return columnWithCommandMap;
    }

}
