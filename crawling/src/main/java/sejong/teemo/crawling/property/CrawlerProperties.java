package sejong.teemo.crawling.property;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties("crawling") // 어떻게 이게 Bean 으로 등록이 되지?
public class CrawlerProperties {

    private final String webUrl;
    private final String remoteIp;
}
