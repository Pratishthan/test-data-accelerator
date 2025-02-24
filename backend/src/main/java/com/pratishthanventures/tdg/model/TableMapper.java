package com.pratishthanventures.tdg.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableMapper extends AbstractConcordionHelper {
    private String tableName;
    private Map<String, String> columnNameMap; // Map<Display Column Name & Technical Column Name for verify>
    private List<Map<String, String>> data;

    public String getConcordionCommand() {
        return "(table)concordion:execute=\"#result=callMapper('" + tableName + "', #ROW)\"";
    }

    public String getConcordionVerifyCommand(String columnName) {
        if (StringUtils.isNotBlank(columnNameMap.get(columnName))){
            return "concordion:assertEquals=\"#result." + columnNameMap.get(columnName) + "\"";
        } else {
            return "";
        }
    }

}
