import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Data
public class JsonData {

    @JsonProperty("type")
    private String type;

    @JsonProperty("name")
    private String name;

    @JsonProperty("headers")
    private List<String> headers = new ArrayList<>();

    @JsonProperty("data")
    private List<Map<String, String>> data = new ArrayList<>();

    // Getters and setters
    // ...
}