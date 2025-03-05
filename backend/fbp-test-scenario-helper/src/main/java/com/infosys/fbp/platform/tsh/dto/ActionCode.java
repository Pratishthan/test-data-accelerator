package com.infosys.fbp.platform.tsh.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.infosys.fbp.platform.tsh.PatternType;
import com.infosys.fbp.platform.tsh.PropertyType;
import com.infosys.fbp.platform.tsh.model.Property;
import lombok.Data;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActionCode {
    private String componentName;
    private String actionCodeGroupName;
    private String actionCode;
    private String endPoint;
    private PatternType type;
    private EnumMap<PropertyType, List<Property>> typeDenormPropertiesMap;
    private EnumMap<PropertyType, Map<String, List<Property>>> typeNormalPropertyMap;
    private List<Map<String, String>> defaultData;
}
