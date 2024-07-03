package sejong.teemo.crawling.common.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "crawling-v2")
public record CrawlingPropertiesV2(String remoteIp) implements CrawlingProperties {
}
