package com.pratishthanventures.tdg.service.converter.factory;

import com.pratishthanventures.tdg.model.FetchAndVerify;
import com.pratishthanventures.tdg.service.converter.processor.FetchAndVerifyPattern;
import lombok.SneakyThrows;

public class FetchAndVerifyFactory extends AbstractFactory {

    @SneakyThrows
    public FetchAndVerifyFactory() {
        getFolderContent("/fetch-and-verify", FetchAndVerify.class)
                .forEach(mapper ->
                        patternMap.put(mapper.getApiName(), new FetchAndVerifyPattern(mapper)));
    }
}
