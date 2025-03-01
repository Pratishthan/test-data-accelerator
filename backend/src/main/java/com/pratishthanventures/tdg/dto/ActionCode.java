package com.pratishthanventures.tdg.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pratishthanventures.tdg.PatternType;
import com.pratishthanventures.tdg.model.Property;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActionCode {
    private String componentName;
    private String actionCodeGroupName;
    private String code;
    private PatternType type;
    private Map<String, Property> columnMap;
    private List<Map<String, String>> defaultData;
}
