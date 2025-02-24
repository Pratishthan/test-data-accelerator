package com.pratishthanventures.tdg.service.converter.processor;

import com.pratishthanventures.tdg.model.TableMapper;
import com.pratishthanventures.tdg.service.converter.Pattern;
import com.pratishthanventures.tdg.util.TDGWorkbook;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.pratishthanventures.tdg.util.TableWithNote.addTableWithNote;

@Slf4j
@AllArgsConstructor
@Getter
public class TableMapperPattern implements Pattern {

    protected TableMapper tableMapper;

    @Override
    public void process(TDGWorkbook workbook, String sheetName, List<String> selectedColumnNames, List<Map<String, String>> data) {
        log.info("TableMapperPattern: About to process {}", tableMapper.getTableName());
        if (!selectedColumnNames.isEmpty()) {
            // filter records from tableMapper.columnNameList based on selectedColumnNames
            tableMapper.setColumnNameMap(tableMapper.getColumnNameMap().entrySet().stream()
                    .filter(entry -> selectedColumnNames.contains(entry.getKey()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
        }
        if(!data.isEmpty()) {
            tableMapper.setData(data);
        }
        addTableWithNote(workbook, sheetName, tableMapper);
    }
}
