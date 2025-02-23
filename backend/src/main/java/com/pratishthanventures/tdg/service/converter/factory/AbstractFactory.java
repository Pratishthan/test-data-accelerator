package com.pratishthanventures.tdg.service.converter.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pratishthanventures.tdg.service.converter.Pattern;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractFactory {

    protected final Map<String, Pattern> patternMap = new HashMap<>();

    protected final ObjectMapper objectMapper = new ObjectMapper();

    public Pattern getPattern(String actionCode) {
        if (actionCode == null) {
            return null;
        }
        if (patternMap.containsKey(actionCode)) {
            return patternMap.get(actionCode);
        }
        throw new IllegalArgumentException("No such action code");
    }

    @SneakyThrows
    public String getContent(String fileName) {
        InputStream inputStream = getClass().getResourceAsStream(fileName);
        assert inputStream != null;
        return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }

    public List<String> getActionCodes(){
        return List.copyOf(patternMap.keySet());
    }

    public Map<String, Pattern> getPatternMap(){
        return Map.copyOf(patternMap);
    }
}
