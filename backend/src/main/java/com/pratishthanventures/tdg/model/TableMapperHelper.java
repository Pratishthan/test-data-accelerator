package com.pratishthanventures.tdg.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class TableMapperHelper implements ConcordionHelper {
    private String tableName;
    private List<String> columnNameList;
    private List<Map<String, String>> data;

    public String getConcordionCommand() {
        return "(table)concordion:execute=\"#result=callMapper('" + tableName + "', #ROW)\"";
    }

}
