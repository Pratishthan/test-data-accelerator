package com.pratishthanventures.tdg.service.converter.factory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.pratishthanventures.tdg.model.FetchAndVerify;
import com.pratishthanventures.tdg.service.converter.processor.FetchAndVerifyPattern;
import lombok.SneakyThrows;

import java.util.List;

public class FetchAndVerifyFactory extends AbstractFactory {

    @SneakyThrows
    public FetchAndVerifyFactory() {
        List<FetchAndVerify> mappers = objectMapper.readValue(
                super.getContent("/FetchAndVerifyList.json"),
                new TypeReference<>() {
                }
        );
        mappers.forEach(mapper -> patternMap.put(mapper.getApiName(), new FetchAndVerifyPattern(mapper)));
    }
}
