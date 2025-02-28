package com.pratishthanventures.tdg.service.converter.factory;

import com.pratishthanventures.tdg.model.TableMapper;
import com.pratishthanventures.tdg.service.converter.processor.TableMapperPattern;
import lombok.SneakyThrows;

public class TableMapperFactory extends AbstractFactory {

    @SneakyThrows
    public TableMapperFactory() {
        getFolderContent("/table-mapper", TableMapper.class)
                .forEach(mapper ->
                        patternMap.put(mapper.getTableName(), new TableMapperPattern(mapper)));
    }
}
