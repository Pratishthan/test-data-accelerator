package com.pratishthanventures.tdg.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pratishthanventures.tdg.PatternType;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActionCode {
    private String code;
    private PatternType type;
    private List<String> columns;
    private List<Map<String, String>> defaultData;
}
