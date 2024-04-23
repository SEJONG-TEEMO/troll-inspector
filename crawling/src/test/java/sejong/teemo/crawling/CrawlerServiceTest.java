package sejong.teemo.crawling;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import sejong.teemo.crawling.property.CrawlingProperties;
import sejong.teemo.crawling.repository.CrawlerRepository;
import sejong.teemo.crawling.service.CrawlerService;

class CrawlerServiceTest extends TestContainer {

    private static final Logger log = LoggerFactory.getLogger(CrawlerServiceTest.class);

    @Autowired
    private FirefoxOptions options;

    @Autowired
    private CrawlerRepository crawlerRepository;

    @Autowired
    private CrawlingProperties crawlingProperties;

    @Autowired
    private CrawlerService crawlerService;

    @Test
    void 크롤링_테스트() {

        log.info("url = {}", crawlingProperties.url());
        log.info("remoteIp = {}", crawlingProperties.remoteIp());

        crawlerService.crawler(1, 100, 5);
    }
}