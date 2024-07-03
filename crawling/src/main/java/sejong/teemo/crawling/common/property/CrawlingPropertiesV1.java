package sejong.teemo.crawling.common.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "crawling-v1")
public record CrawlingPropertiesV1(String remoteIp) implements CrawlingProperties {
}
