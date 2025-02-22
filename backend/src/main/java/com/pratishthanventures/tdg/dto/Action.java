package com.pratishthanventures.tdg.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Action {

    @JsonProperty("type")
    private String type;

    @JsonProperty("name")
    private String name;

    @JsonProperty("action")
    private String action;

    @JsonProperty("tableInfo")
    private TableAction tableAction;

    @JsonProperty("apiInfo")
    private ApiAction apiAction;

    @JsonProperty("validateInfo")
    private ValidateAction validateAction;
}
