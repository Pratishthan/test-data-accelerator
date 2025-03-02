package com.infosys.fbp.platform.tsh.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.infosys.fbp.platform.tsh.PatternType;
import com.infosys.fbp.platform.tsh.model.Property;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActionCode {
    private String componentName;
    private String actionCodeGroupName;
    private String actionCode;
    private PatternType type;
    private Map<String, Property> columns;
    private List<Map<String, String>> defaultData;
}
