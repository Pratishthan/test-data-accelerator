package com.pratishthanventures.tdg.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pratishthanventures.tdg.model.CommandChain;
import com.pratishthanventures.tdg.service.converter.Pattern;
import com.pratishthanventures.tdg.service.converter.factory.AbstractFactory;
import com.pratishthanventures.tdg.service.converter.factory.FactoryProducer;
import com.pratishthanventures.tdg.util.TDGWorkbook;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static com.pratishthanventures.tdg.PatternType.*;

@Slf4j
public class ActionChainConverter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    public String getContent(String fileName) {
        InputStream inputStream = getClass().getResourceAsStream(fileName);
        assert inputStream != null;
        return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }

    @SneakyThrows
    public void process() {

        AbstractFactory simpleCommandFactory = FactoryProducer.getFactory(SimpleCommand);
        AbstractFactory tableMapperFactory = FactoryProducer.getFactory(TableMapper);
        AbstractFactory fetchAndVerifyFactory = FactoryProducer.getFactory(FetchAndVerify);
        AbstractFactory setAndExecuteFactory = FactoryProducer.getFactory(SetAndExecute);
        try {
            CommandChain commandChain = objectMapper.readValue(getContent("/CommandChain.json"), CommandChain.class);
            TDGWorkbook workbook = new TDGWorkbook(commandChain.getSheetName());

            commandChain.getCommands().forEach(command -> {
                Pattern pattern = switch (command.getType()) {
                    case SimpleCommand -> simpleCommandFactory.getPattern(command.getActionCode());
                    case TableMapper -> tableMapperFactory.getPattern(command.getActionCode());
                    case FetchAndVerify -> fetchAndVerifyFactory.getPattern(command.getActionCode());
                    case SetAndExecute -> setAndExecuteFactory.getPattern(command.getActionCode());
                };

                pattern.process(workbook, commandChain.getSheetName(), command.getSelectedColumns(), command.getData());
            });
            log.info("About to write workbook to file {}", commandChain.getExcelFileName());
            workbook.writeWorkbookToFile(commandChain.getExcelFileName());
        } catch (Exception e) {
            log.error("Error reading CommandChain.json {}", e.getMessage());
        }

    }


}
