import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class Spec {

    @JsonProperty("tables")
    private List<JsonData> tables = new ArrayList<>();

    @JsonProperty("action")
    private String action;

    // Getters and setters
    // ...
}