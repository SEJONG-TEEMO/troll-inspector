package sejong.teemo.trollinspector.util;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "secret")
@Getter
@RequiredArgsConstructor
public class ConfigProperties {

    private final Elasticsearch elasticsearch;
    private final Riot riot;

    @Data
    public static class Elasticsearch {
        private String scheme;
        private String host;
        private int port;
        private String apikey;
    }

    @Data
    public static class Riot {
        private String apikey;
    }
}

