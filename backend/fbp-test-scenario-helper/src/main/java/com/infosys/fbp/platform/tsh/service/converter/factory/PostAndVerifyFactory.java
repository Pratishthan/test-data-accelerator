package com.infosys.fbp.platform.tsh.service.converter.factory;

import com.infosys.fbp.platform.tsh.PatternType;
import com.infosys.fbp.platform.tsh.model.PostAndVerify;
import com.infosys.fbp.platform.tsh.service.converter.processor.PostAndVerifyPattern;
import com.infosys.fbp.platform.tsh.service.parser.OpenAPISpecParser;
import lombok.SneakyThrows;

public class PostAndVerifyFactory extends AbstractFactory {


    @SneakyThrows
    public PostAndVerifyFactory() {
        OpenAPISpecParser openAPISpecParser = new OpenAPISpecParser();
        openAPISpecParser.componentActionCodeMap().forEach((component, ac_map) ->
                ac_map.forEach((op, ac) -> {
                    if (PatternType.PostAndVerify.equals(ac.getType())) {
                        PostAndVerify postAndVerify = new PostAndVerify();
                        postAndVerify.setTypeDenormPropertiesMap(ac.getTypeDenormPropertiesMap());
                        postAndVerify.setTypeNormalPropertyMap(ac.getTypeNormalPropertyMap());
                        postAndVerify.setApiName(ac.getActionCode());
                        PostAndVerifyPattern postAndVerifyPattern = new PostAndVerifyPattern(postAndVerify);
                        patternMap.put(op, postAndVerifyPattern);
                    }
                }));
    }
}
