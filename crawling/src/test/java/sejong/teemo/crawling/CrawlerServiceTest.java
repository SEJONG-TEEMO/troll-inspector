package sejong.teemo.crawling;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import sejong.teemo.crawling.property.CrawlerProperties;
import sejong.teemo.crawling.repository.CrawlerRepository;
import sejong.teemo.crawling.service.CrawlerService;

class CrawlerServiceTest extends TestContainer {

    private static final Logger log = LoggerFactory.getLogger(CrawlerServiceTest.class);

    @Autowired
    private FirefoxOptions options;

    @Autowired
    private CrawlerRepository crawlerRepository;

    @Autowired
    private CrawlerProperties crawlerProperties;

    @Autowired
    private CrawlerService crawlerService;

    @Test
    void 크롤링_테스트() {

        log.info("url = {}", crawlerProperties.getWebUrl());
        log.info("remoteIp = {}", crawlerProperties.getRemoteIp());

        crawlerService.crawler(1, 1, 1);
    }
}