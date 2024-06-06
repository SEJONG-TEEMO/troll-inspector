package sejong.teemo.trollinspector.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

@Slf4j
public class QueryLoader {
    public static String loadJsonQuery(String fileName) {
        try {
            URL resourceUrl = Optional.ofNullable(QueryLoader.class.getClassLoader().getResource(fileName))
                    .orElseThrow(() -> new IOException("Resource file not found"));
            return new String(Files.readAllBytes(Paths.get(resourceUrl.toURI())));
        } catch (IOException | URISyntaxException e) {
            log.error("Failed to read the query file", e);
            throw new RuntimeException(e);
        }
    }
}
