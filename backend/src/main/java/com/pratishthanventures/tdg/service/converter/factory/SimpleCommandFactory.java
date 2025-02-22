package com.pratishthanventures.tdg.service.converter.factory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.pratishthanventures.tdg.model.SimpleCommandHelper;
import com.pratishthanventures.tdg.service.converter.strategy.SimpleCommandPattern;
import lombok.SneakyThrows;

import java.util.List;

public class SimpleCommandFactory extends AbstractFactory {

    @SneakyThrows
    public SimpleCommandFactory() {
        List<SimpleCommandHelper> mappers = objectMapper.readValue(
                super.getContent("/SimpleCommands.json"),
                new TypeReference<>() {
                }
        );
        mappers.forEach(mapper -> patternMap.put(mapper.getText(), new SimpleCommandPattern(mapper)));
    }
}
