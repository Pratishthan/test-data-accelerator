package com.infosys.fbp.platform.tsh.service.converter.factory;

import com.infosys.fbp.platform.tsh.model.SetAndExecute;
import com.infosys.fbp.platform.tsh.service.converter.processor.SetAndExecutePattern;
import lombok.SneakyThrows;

public class SetAndExecuteFactory extends AbstractFactory {

    @SneakyThrows
    public SetAndExecuteFactory() {
        getFolderContent("/set-and-execute", SetAndExecute.class)
                .forEach(mapper ->
                        patternMap.put(mapper.getFunctionName(), new SetAndExecutePattern(mapper)));
    }
}
