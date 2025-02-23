package com.pratishthanventures.tdg.service.converter.factory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.pratishthanventures.tdg.model.SetAndExecute;
import com.pratishthanventures.tdg.service.converter.processor.SetAndExecutePattern;
import lombok.SneakyThrows;

import java.util.List;

public class SetAndExecuteFactory extends AbstractFactory {

    @SneakyThrows
    public SetAndExecuteFactory() {
        List<SetAndExecute> mappers = objectMapper.readValue(
                super.getContent("/SetAndExecuteList.json"),
                new TypeReference<>() {
                }
        );
        mappers.forEach(mapper -> patternMap.put(mapper.getFunctionName(), new SetAndExecutePattern(mapper)));
    }
}
