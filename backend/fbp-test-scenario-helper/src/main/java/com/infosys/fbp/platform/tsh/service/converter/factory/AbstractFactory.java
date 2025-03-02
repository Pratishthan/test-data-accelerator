package com.infosys.fbp.platform.tsh.service.converter.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infosys.fbp.platform.tsh.service.converter.Pattern;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

public abstract class AbstractFactory {

    protected final Map<String, Pattern> patternMap = new HashMap<>();

    protected final ObjectMapper objectMapper = new ObjectMapper();

    public Pattern getPattern(String actionCode) {
        if (actionCode == null) {
            return null;
        }
        if (patternMap.containsKey(actionCode)) {
            return patternMap.get(actionCode);
        }
        throw new IllegalArgumentException("No such action code");
    }

    @SneakyThrows
    public <T> List<T> getFolderContent(String folderName, Class<T> clazz) {
        List<T> result = new ArrayList<>();

        // Get the resource folder URL
        URL folderUrl = getClass().getResource(folderName);
        if (folderUrl == null) {
            throw new IllegalArgumentException("Folder not found: " + folderName);
        }

        // Convert URL to URI and create Path
        URI uri = folderUrl.toURI();
        Path folderPath;
        FileSystem fileSystem = null;

        try {
            if (uri.getScheme().equals("jar")) {
                fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
                folderPath = fileSystem.getPath(folderName);
            } else {
                folderPath = Paths.get(uri);
            }
        } finally {
            if (fileSystem != null) {
                fileSystem.close();
            }
        }

        try (Stream<Path> paths = Files.walk(folderPath, 1)) {
            // Walk through the folder and process each file
            paths
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        try (InputStream inputStream = getClass().getResourceAsStream(
                                folderName + "/" + path.getFileName())) {
                            if (inputStream != null) {
                                String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                                T object = objectMapper.readValue(content, clazz);
                                result.add(object);
                            }
                        } catch (Exception e) {
                            throw new RuntimeException("Error reading file: " + path, e);
                        }
                    });
        }

        return result;
    }


    public Map<String, Pattern> getPatternMap() {
        return Map.copyOf(patternMap);
    }
}
