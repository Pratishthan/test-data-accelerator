package com.pratishthanventures.tdg.service.converter.factory;

import com.pratishthanventures.tdg.model.SimpleCommand;
import com.pratishthanventures.tdg.service.converter.processor.SimpleCommandPattern;
import lombok.SneakyThrows;

public class SimpleCommandFactory extends AbstractFactory {

    @SneakyThrows
    public SimpleCommandFactory() {
        getFolderContent("/simple-command", SimpleCommand.class)
                .forEach(mapper ->
                        patternMap.put(mapper.getText(), new SimpleCommandPattern(mapper)));
    }
}
