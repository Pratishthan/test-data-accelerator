package com.pratishthanventures.tdg.service.converter;

import com.pratishthanventures.tdg.service.converter.factory.AbstractFactory;
import com.pratishthanventures.tdg.service.converter.factory.FactoryProducer;

public class PatternDemo {

    public static void main(String[] args) {
        AbstractFactory tableMapperFactory = FactoryProducer.getFactory("TableMapper");
        Pattern collectableEvent = tableMapperFactory.getPattern("CollectableEvent");
        collectableEvent.process(null, null);
    }
}
