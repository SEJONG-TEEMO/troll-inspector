package sejong.teemo.trollinspector.util;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "secret")
public record ConfigProperties(Elasticsearch elasticsearch,
        Riot riot, Document document) {
    public record Elasticsearch(String scheme, String host, int port, String apikey) {}
    public record Riot(String apikey) {}
    public record Document(String indexName) {}
}

