package com.pratishthanventures.tdg.model;


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
    private String actionCode;
    private String type;
    private List<String> selectedColumns = new ArrayList<>();
    private List<Map<String, String>> data = new ArrayList<>();
}
