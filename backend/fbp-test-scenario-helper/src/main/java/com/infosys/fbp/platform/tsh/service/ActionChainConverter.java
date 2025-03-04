package com.infosys.fbp.platform.tsh.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infosys.fbp.platform.tsh.model.CommandChain;
import com.infosys.fbp.platform.tsh.service.converter.Pattern;
import com.infosys.fbp.platform.tsh.service.converter.factory.AbstractFactory;
import com.infosys.fbp.platform.tsh.service.converter.factory.FactoryProducer;
import com.infosys.fbp.platform.tsh.util.TDGWorkbook;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static com.infosys.fbp.platform.tsh.PatternType.*;

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
    public TDGWorkbook process(String resourcePath) {
        CommandChain commandChain = objectMapper.readValue(getContent(resourcePath), CommandChain.class);
        TDGWorkbook workbook = process(commandChain);
        log.info("About to write workbook to file {}", commandChain.getExcelFileName());
        workbook.writeWorkbookToFile();
        return workbook;
    }

    @SneakyThrows
    public TDGWorkbook process(CommandChain commandChain) {

        AbstractFactory simpleCommandFactory = FactoryProducer.getFactory(SimpleCommand);
        AbstractFactory tableMapperFactory = FactoryProducer.getFactory(TableMapper);
        AbstractFactory postAndVerifyFactory = FactoryProducer.getFactory(PostAndVerify);
        AbstractFactory fetchAndVerifyFactory = FactoryProducer.getFactory(FetchAndVerify);
        AbstractFactory setAndExecuteFactory = FactoryProducer.getFactory(SetAndExecute);
        try {
            TDGWorkbook workbook = new TDGWorkbook(commandChain.getSheetName(), commandChain.getExcelFileName());

            commandChain.getCommands().forEach((commandName, command) -> {
                log.info("About to process {}", command.getActionCode());
                Pattern pattern = switch (command.getType()) {
                    case SimpleCommand -> simpleCommandFactory.getPattern(command.getActionCode());
                    case TableMapper -> tableMapperFactory.getPattern(command.getActionCode());
                    case PostAndVerify -> postAndVerifyFactory.getPattern(command.getActionCode());
                    case FetchAndVerify -> fetchAndVerifyFactory.getPattern(command.getActionCode());
                    case SetAndExecute -> setAndExecuteFactory.getPattern(command.getActionCode());
                };

                pattern.process(workbook, commandChain, commandName);
            });
            return workbook;
        } catch (Exception e) {
            log.error("Error reading CommandChain.json {}", e.getMessage());
        }

        return null;

    }


}
