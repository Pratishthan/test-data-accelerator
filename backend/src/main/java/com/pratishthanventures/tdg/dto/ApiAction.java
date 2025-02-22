package com.pratishthanventures.tdg.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class ApiAction{

    @JsonProperty("params")
    private List<Map<String, String>> params = new ArrayList<>();

}
