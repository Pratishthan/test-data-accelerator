package com.infosys.fbp.platform.tsh.service.converter.factory;

import com.infosys.fbp.platform.tsh.model.FetchAndVerify;
import com.infosys.fbp.platform.tsh.service.converter.processor.FetchAndVerifyPattern;
import lombok.SneakyThrows;

public class FetchAndVerifyFactory extends AbstractFactory {

    @SneakyThrows
    public FetchAndVerifyFactory() {
        getFolderContent("/fetch-and-verify", FetchAndVerify.class)
                .forEach(mapper ->
                        patternMap.put(mapper.getApiName(), new FetchAndVerifyPattern(mapper)));
    }
}
