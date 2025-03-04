package com.infosys.fbp.platform.tsh.service.converter.processor;

import com.infosys.fbp.platform.tsh.model.AbstractConcordionHelper;
import com.infosys.fbp.platform.tsh.model.Command;
import com.infosys.fbp.platform.tsh.model.CommandChain;
import com.infosys.fbp.platform.tsh.model.TableMapper;
import com.infosys.fbp.platform.tsh.service.converter.Pattern;
import com.infosys.fbp.platform.tsh.util.TDGWorkbook;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.infosys.fbp.platform.tsh.util.TableWithNote.addTableWithNote;

@Slf4j
@AllArgsConstructor
@Getter
public class TableMapperPattern implements Pattern {

    protected TableMapper tableMapper;

    @Override
    public void process(TDGWorkbook workbook, CommandChain commandChain, String commandName) {
        String sheetName = commandChain.getSheetName();
        Command command = commandChain.getCommands().get(commandName);
        List<String> selectedColumnNames = new ArrayList<>(); // TODO: read from EnumMap<PropertyType, List<Property>> propertyListMap;
        log.info("TableMapperPattern: About to process {}", tableMapper.getTableName());
        if (!selectedColumnNames.isEmpty()) {
            // filter records from tableMapper.columnNameList based on selectedColumnNames
            tableMapper.setColumnNameMap(tableMapper.getColumnNameMap().entrySet().stream()
                    .filter(entry -> selectedColumnNames.contains(entry.getKey()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
        }
        if (!command.getData().isEmpty()) {
            tableMapper.setData(command.getData());
        }
        addTableWithNote(workbook, sheetName, commandName, tableMapper);
    }

    @Override
    public AbstractConcordionHelper getPattern(){
        return this.tableMapper;
    }
}
