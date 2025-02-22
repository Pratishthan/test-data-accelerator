package com.pratishthanventures.tdg.service.converter.factory;

import com.pratishthanventures.tdg.model.TableMapperHelper;
import com.pratishthanventures.tdg.service.converter.Pattern;
import com.pratishthanventures.tdg.service.converter.strategy.TableMapperPattern;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableMapperFactory extends AbstractFactory {


    private final Map<String, TableMapperPattern> tableMapperPatternMap = new HashMap<>();

    public TableMapperFactory() {
        List<TableMapperHelper> mappers = super.getContent("/TableMappers.json");
        mappers.forEach(mapper -> {
            tableMapperPatternMap.put(mapper.getTableName(), new TableMapperPattern(mapper));
        });
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
