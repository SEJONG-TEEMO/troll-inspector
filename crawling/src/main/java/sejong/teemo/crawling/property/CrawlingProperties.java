package sejong.teemo.crawling.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "crawling")
public record CrawlingProperties(String url, String remoteIp) {
}
