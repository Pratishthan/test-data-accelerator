package com.pratishthanventures.tdg.service.converter.factory;

public class FactoryProducer {
    public static AbstractFactory getFactory(String pattern) {
        return switch (pattern) {
            case "SimpleCommand" -> new SimpleCommandFactory();
            case "TableMapper" -> new TableMapperFactory();
            case "FetchAndVerify" -> new FetchAndVerifyFactory();
            case "SetAndExecute" -> new SetAndExecuteFactory();
            default -> throw new IllegalArgumentException("No such factory");
        };
    }
}
