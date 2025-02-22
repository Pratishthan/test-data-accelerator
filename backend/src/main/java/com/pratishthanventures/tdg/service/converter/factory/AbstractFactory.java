package com.pratishthanventures.tdg.service.converter.factory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pratishthanventures.tdg.service.converter.Pattern;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public abstract class AbstractFactory {
    public abstract Pattern getPattern(String patternType);

    @SneakyThrows
    public <T> List<T> getContent(String fileName) {
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream inputStream = getClass().getResourceAsStream(fileName);
        assert inputStream != null;
        return
        objectMapper.readValue(
                new String(inputStream.readAllBytes(), StandardCharsets.UTF_8),
                new TypeReference<>() {}
        );
    }
}
