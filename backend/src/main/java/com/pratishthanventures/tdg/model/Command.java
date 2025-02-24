package com.pratishthanventures.tdg.model;


import com.pratishthanventures.tdg.PatternType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Command {
    private String commandName;
    private String actionCode;
    private PatternType type;
    private List<String> selectedColumns = new ArrayList<>();
    private List<Map<String, String>> data = new ArrayList<>();
}
