package com.pratishthanventures.tdg.service.converter.factory;

public class FactoryProducer {
    public static AbstractFactory getFactory(String pattern) {
        if ("TableMapper".equalsIgnoreCase(pattern)) {
            return new TableMapperFactory();
        }
        return null;
    }
}
