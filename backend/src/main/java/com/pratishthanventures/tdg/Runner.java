package com.pratishthanventures.tdg;

import com.pratishthanventures.tdg.service.ActionChainConverter;
import com.pratishthanventures.tdg.util.TDGWorkbook;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class Runner {
    public static void main(String[] args) {
        ActionChainConverter actionChainConverter = new ActionChainConverter();
        TDGWorkbook workbook = actionChainConverter.process("/CommandChain.json");
        log.info("Completed writing Excel file {}", workbook.getFileName());
    }
}
