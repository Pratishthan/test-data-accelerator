package com.pratishthanventures.tdg.service.converter.factory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.pratishthanventures.tdg.model.SimpleCommandHelper;
import com.pratishthanventures.tdg.service.converter.Pattern;
import com.pratishthanventures.tdg.service.converter.strategy.SimpleCommandPattern;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleCommandFactory extends AbstractFactory {


    private final Map<String, SimpleCommandPattern> simpleCommandPatternHashMap = new HashMap<>();

    @SneakyThrows
    public SimpleCommandFactory() {
        List<SimpleCommandHelper> mappers = objectMapper.readValue(
                super.getContent("/SimpleCommands.json"),
                new TypeReference<>() {
                }
        );
        mappers.forEach(mapper -> simpleCommandPatternHashMap.put(mapper.getText(), new SimpleCommandPattern(mapper)));
    }

    @Override
    public Pattern getPattern(String actionCode) {
        if (actionCode == null) {
            return null;
        }
        if (simpleCommandPatternHashMap.containsKey(actionCode)) {
            return simpleCommandPatternHashMap.get(actionCode);
        }
        return null;
    }
}
