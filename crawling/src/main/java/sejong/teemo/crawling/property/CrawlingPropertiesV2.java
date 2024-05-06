package sejong.teemo.crawling.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "crawling-v2")
public record CrawlingPropertiesV2(String url, String remoteIp) implements CrawlingProperties {
}
