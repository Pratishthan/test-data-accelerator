package com.pratishthanventures.tdg.dto;

import com.pratishthanventures.tdg.PatternType;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ActionCode {
    private String code;
    private PatternType type;
    private List<String> columns;
    private List<Map<String, String>> defaultData;
}
