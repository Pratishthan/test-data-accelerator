package com.infosys.fbp.platform.tsh.service.converter.factory;

import com.infosys.fbp.platform.tsh.model.TableMapper;
import com.infosys.fbp.platform.tsh.service.converter.processor.TableMapperPattern;
import lombok.SneakyThrows;

public class TableMapperFactory extends AbstractFactory {

    @SneakyThrows
    public TableMapperFactory() {
        getFolderContent("/table-mapper", TableMapper.class)
                .forEach(mapper ->
                        patternMap.put(mapper.getTableName(), new TableMapperPattern(mapper)));
    }
}
