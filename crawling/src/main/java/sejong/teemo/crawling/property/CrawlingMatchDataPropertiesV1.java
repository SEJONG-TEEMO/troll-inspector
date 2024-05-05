package sejong.teemo.crawling.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "crawling-match-v1")
public record CrawlingMatchDataPropertiesV1(String url, String remoteIp) {

    @Override
    public String toString() {
        return "CrawlingMatchDataPropertiesV1{" +
                "url='" + url + '\'' +
                ", remoteIp='" + remoteIp + '\'' +
                '}';
    }
}
