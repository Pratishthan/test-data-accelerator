package com.pratishthanventures.tdg.service.converter;

import com.pratishthanventures.tdg.output.TDGWorkbook;
import com.pratishthanventures.tdg.service.converter.factory.AbstractFactory;
import com.pratishthanventures.tdg.service.converter.factory.FactoryProducer;

public class PatternDemo {

    public static void main(String[] args) {
        String excelFilePath = "output.xlsx";

        TDGWorkbook workbook = new TDGWorkbook("Data");

        AbstractFactory tableMapperFactory = FactoryProducer.getFactory("TableMapper");
        Pattern collectableEvent = tableMapperFactory.getPattern("Collectable Event");
        collectableEvent.process(workbook, "Data");

        Pattern demandCode = tableMapperFactory.getPattern("Demand Code");
        demandCode.process(workbook, "Data");

        workbook.writeWorkbookToFile(excelFilePath);

    }
}
