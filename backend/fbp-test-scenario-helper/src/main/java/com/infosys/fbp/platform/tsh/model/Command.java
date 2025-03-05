package com.infosys.fbp.platform.tsh.model;


import com.infosys.fbp.platform.tsh.PatternType;
import com.infosys.fbp.platform.tsh.PropertyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Command {
    private String commandName;
    private String actionCode;
    private PatternType type;
    private EnumMap<PropertyType, List<Property>> selectPropertyListMap;
    private EnumMap<PropertyType, InputType> inputTypeMap = denormMap();
    private List<Map<String, String>> data = new ArrayList<>();

    private EnumMap<PropertyType, InputType> denormMap() {
        EnumMap<PropertyType, InputType> denormMap = new EnumMap<>(PropertyType.class);

        denormMap.put(PropertyType.RequestBodyColumnList, InputType.Denorm);
        denormMap.put(PropertyType.ResponseBodyColumnList, InputType.Denorm);
        denormMap.put(PropertyType.PathParamList, InputType.Denorm);
        denormMap.put(PropertyType.QueryParamList, InputType.Denorm);

        return denormMap;
    }
}
