package sejong.teemo.crawling;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sejong.teemo.crawling.property.CrawlingProperties;
import sejong.teemo.crawling.repository.CrawlerRepository;
import sejong.teemo.crawling.service.CrawlerService;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class CrawlerServiceDataTest {

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

        crawlerService.crawler(320, 400, 5);
    }
}
