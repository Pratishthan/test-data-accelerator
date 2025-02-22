package com.pratishthanventures.tdg.service.converter.factory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.pratishthanventures.tdg.model.TableMapper;
import com.pratishthanventures.tdg.service.converter.processor.TableMapperPattern;
import lombok.SneakyThrows;

import java.util.List;

public class TableMapperFactory extends AbstractFactory {

    @SneakyThrows
    public TableMapperFactory() {
        List<TableMapper> mappers = objectMapper.readValue(
                super.getContent("/TableMappers.json"),
                new TypeReference<>() {
                }
        );
        mappers.forEach(mapper -> patternMap.put(mapper.getTableName(), new TableMapperPattern(mapper)));
    }
}
