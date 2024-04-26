package sejong.teemo.crawling.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "crawling-v1")
public record CrawlingPropertiesV1(String url, String remoteIp) implements CrawlingProperties {
}
