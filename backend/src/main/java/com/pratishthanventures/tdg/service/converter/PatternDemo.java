package com.pratishthanventures.tdg.service.converter;

import com.pratishthanventures.tdg.output.TDGWorkbook;
import com.pratishthanventures.tdg.service.converter.factory.AbstractFactory;
import com.pratishthanventures.tdg.service.converter.factory.FactoryProducer;

public class PatternDemo {

    public static void main(String[] args) {
        String excelFilePath = "output.xlsx";

        TDGWorkbook workbook = new TDGWorkbook("Data");

        AbstractFactory simpleCommandFactory = FactoryProducer.getFactory("SimpleCommand");
        AbstractFactory tableMapperFactory = FactoryProducer.getFactory("TableMapper");

        simpleCommandFactory.getPattern("We have a clean DB").process(workbook, "Data");

        tableMapperFactory.getPattern("Collectable Event").process(workbook, "Data");

        tableMapperFactory.getPattern("Demand Code").process(workbook, "Data");

        workbook.writeWorkbookToFile(excelFilePath);

    }
}
