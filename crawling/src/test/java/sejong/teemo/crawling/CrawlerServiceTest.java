package sejong.teemo.crawling;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sejong.teemo.crawling.repository.CrawlerRepository;
import sejong.teemo.crawling.service.CrawlerService;

class CrawlerServiceTest extends TestContainer {

    @Value("web.url")
    private String url;

    @Value("remote.ip")
    private String remoteIp;

    @Autowired
    private FirefoxOptions options;

    @Autowired
    private CrawlerRepository crawlerRepository;

    @Test
    void 크롤링_테스트() {
        CrawlerService crawlerService = new CrawlerService(options, crawlerRepository);

        crawlerService.crawler(url, remoteIp, 1, 1, 1);
    }
}