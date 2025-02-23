package com.pratishthanventures.tdg.service.converter.processor;

import com.pratishthanventures.tdg.model.TableMapper;
import com.pratishthanventures.tdg.service.converter.Pattern;
import com.pratishthanventures.tdg.util.TDGWorkbook;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

import static com.pratishthanventures.tdg.util.TableWithNote.addTableWithNote;

@Slf4j
@AllArgsConstructor
public class TableMapperPattern implements Pattern {

    protected TableMapper tableMapper;

    @Override
    public void process(TDGWorkbook workbook, String sheetName, List<String> columnNames, List<Map<String, String>> data) {
        log.info("TableMapperPattern: About to process {}", tableMapper.getTableName());
        if (!columnNames.isEmpty()) {
            tableMapper.setColumnNameList(columnNames);
        }
        if(!data.isEmpty()) {
            tableMapper.setData(data);
        }
        addTableWithNote(workbook, sheetName, tableMapper);
    }
}
