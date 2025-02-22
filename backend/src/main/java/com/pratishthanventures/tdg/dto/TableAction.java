package com.pratishthanventures.tdg.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class TableAction{
    @JsonProperty("headers")
    private List<TableHeader> headers = new ArrayList<>();

    @JsonProperty("data")
    private List<Map<String, String>> data = new ArrayList<>();
}
