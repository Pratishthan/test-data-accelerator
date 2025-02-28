package com.pratishthanventures.tdg;

import com.pratishthanventures.tdg.service.ActionChainConverter;

public class Runner {
    public static void main(String[] args) {
        ActionChainConverter actionChainConverter = new ActionChainConverter();
        actionChainConverter.process();
    }
}
