package com.infosys.fbp.platform.tsh.service.converter.factory;

import com.infosys.fbp.platform.tsh.PatternType;

public class FactoryProducer {
    public static AbstractFactory getFactory(PatternType pattern) {
        return switch (pattern) {
            case SimpleCommand -> new SimpleCommandFactory();
            case TableMapper -> new TableMapperFactory();
            case PostAndVerify -> new PostAndVerifyFactory();
            case FetchAndVerify -> new FetchAndVerifyFactory();
            case SetAndExecute -> new SetAndExecuteFactory();
        };
    }
}
