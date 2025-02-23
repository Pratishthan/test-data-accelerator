package com.pratishthanventures.tdg.service.converter.factory;

import com.pratishthanventures.tdg.PatternType;

public class FactoryProducer {
    public static AbstractFactory getFactory(PatternType pattern) {
        return switch (pattern) {
            case SimpleCommand -> new SimpleCommandFactory();
            case TableMapper -> new TableMapperFactory();
            case FetchAndVerify -> new FetchAndVerifyFactory();
            case SetAndExecute -> new SetAndExecuteFactory();
        };
    }
}
