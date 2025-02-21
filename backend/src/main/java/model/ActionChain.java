package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ActionChain {

    @JsonProperty
    List<Action> data;
}
