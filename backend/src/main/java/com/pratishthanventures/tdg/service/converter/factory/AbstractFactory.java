package com.pratishthanventures.tdg.service.converter.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pratishthanventures.tdg.service.converter.Pattern;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public abstract class AbstractFactory {
    protected final ObjectMapper objectMapper = new ObjectMapper();

    public abstract Pattern getPattern(String patternType);

    @SneakyThrows
    public String getContent(String fileName) {
        InputStream inputStream = getClass().getResourceAsStream(fileName);
        assert inputStream != null;
        return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }
}
