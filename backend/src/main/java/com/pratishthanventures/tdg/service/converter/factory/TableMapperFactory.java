package com.pratishthanventures.tdg.service.converter.factory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.pratishthanventures.tdg.model.TableMapperHelper;
import com.pratishthanventures.tdg.service.converter.Pattern;
import com.pratishthanventures.tdg.service.converter.strategy.TableMapperPattern;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableMapperFactory extends AbstractFactory {


    private final Map<String, TableMapperPattern> tableMapperPatternMap = new HashMap<>();

    @SneakyThrows
    public TableMapperFactory() {
        List<TableMapperHelper> mappers = objectMapper.readValue(
                super.getContent("/TableMappers.json"),
                new TypeReference<>() {
                }
        );
        mappers.forEach(mapper -> tableMapperPatternMap.put(mapper.getTableName(), new TableMapperPattern(mapper)));
    }

    @Override
    public Pattern getPattern(String actionCode) {
        if (actionCode == null) {
            return null;
        }
        if (tableMapperPatternMap.containsKey(actionCode)) {
            return tableMapperPatternMap.get(actionCode);
        }
        return null;
    }
}
