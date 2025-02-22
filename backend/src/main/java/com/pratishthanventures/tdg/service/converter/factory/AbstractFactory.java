package com.pratishthanventures.tdg.service.converter.factory;

import com.pratishthanventures.tdg.service.converter.Pattern;

public abstract class AbstractFactory {
    public abstract Pattern getPattern(String patternType);
}
