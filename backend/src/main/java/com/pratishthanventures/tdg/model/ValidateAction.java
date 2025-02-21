package com.pratishthanventures.tdg.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ValidateAction {

    @JsonProperty("params")
    List<Map<String, String>> params = new ArrayList<>();
}
