package com.pratishthanventures.tdg.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TableHeader {
    @JsonProperty("name")
    private String name;

    @JsonProperty("sanityAction")
    private String sanityAction;
}
