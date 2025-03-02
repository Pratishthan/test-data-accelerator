package com.infosys.fbp.platform.tsh.service.converter.factory;

import com.infosys.fbp.platform.tsh.model.SimpleCommand;
import com.infosys.fbp.platform.tsh.service.converter.processor.SimpleCommandPattern;
import lombok.SneakyThrows;

public class SimpleCommandFactory extends AbstractFactory {

    @SneakyThrows
    public SimpleCommandFactory() {
        getFolderContent("/simple-command", SimpleCommand.class)
                .forEach(mapper ->
                        patternMap.put(mapper.getText(), new SimpleCommandPattern(mapper)));
    }
}
