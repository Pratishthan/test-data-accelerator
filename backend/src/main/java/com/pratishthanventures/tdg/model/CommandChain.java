package com.pratishthanventures.tdg.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommandChain {

    private String excelFileName;
    private String sheetName;
    private Map<String, Command> commands = new HashMap<>();
}
