package com.infosys.fbp.platform.tsh.service.converter.factory;

import com.infosys.fbp.platform.tsh.PatternType;
import com.infosys.fbp.platform.tsh.model.FetchAndVerify;
import com.infosys.fbp.platform.tsh.service.converter.processor.FetchAndVerifyPattern;
import com.infosys.fbp.platform.tsh.service.parser.OpenAPISpecParser;
import lombok.SneakyThrows;

public class FetchAndVerifyFactory extends AbstractFactory {

    @SneakyThrows
    public FetchAndVerifyFactory() {
        OpenAPISpecParser openAPISpecParser = new OpenAPISpecParser();
        openAPISpecParser.componentActionCodeMap().forEach((component, ac_map) ->
                ac_map.forEach((op, ac) -> {
                    if (PatternType.FetchAndVerify.equals(ac.getType())) {
                        FetchAndVerify fetchAndVerify = new FetchAndVerify();
                        fetchAndVerify.setPropertyListMap(ac.getPropertyListMap());
                        fetchAndVerify.setApiName(ac.getActionCode());
                        FetchAndVerifyPattern fetchAndVerifyPattern = new FetchAndVerifyPattern(fetchAndVerify);
                        patternMap.put(op, fetchAndVerifyPattern);
                    }
                }));
    }
}
