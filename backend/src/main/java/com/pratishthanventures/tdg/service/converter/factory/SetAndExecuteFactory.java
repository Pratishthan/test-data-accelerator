package com.pratishthanventures.tdg.service.converter.factory;

import com.pratishthanventures.tdg.model.SetAndExecute;
import com.pratishthanventures.tdg.service.converter.processor.SetAndExecutePattern;
import lombok.SneakyThrows;

public class SetAndExecuteFactory extends AbstractFactory {

    @SneakyThrows
    public SetAndExecuteFactory() {
        getFolderContent("/set-and-execute", SetAndExecute.class)
                .forEach(mapper ->
                        patternMap.put(mapper.getFunctionName(), new SetAndExecutePattern(mapper)));
    }
}
