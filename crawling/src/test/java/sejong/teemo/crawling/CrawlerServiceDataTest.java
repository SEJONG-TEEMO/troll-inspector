package sejong.teemo.crawling;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sejong.teemo.crawling.facade.CrawlerFacade;
import sejong.teemo.crawling.property.CrawlingProperties;
import sejong.teemo.crawling.property.CrawlingPropertiesV1;
import sejong.teemo.crawling.property.CrawlingPropertiesV2;
import sejong.teemo.crawling.repository.CrawlerRepository;
import sejong.teemo.crawling.service.CrawlerService;
import sejong.teemo.crawling.webDriver.generator.UrlGenerator;

import java.util.List;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class CrawlerServiceDataTest {

    private static final Logger log = LoggerFactory.getLogger(CrawlerServiceDataTest.class);

    @Autowired
    private CrawlerRepository crawlerRepository;

    @Autowired
    private CrawlingPropertiesV1 crawlingPropertiesV1;

    @Autowired
    private CrawlingPropertiesV2 crawlingPropertiesV2;

    @Autowired
    private List<CrawlingProperties> properties;

    @Autowired
    private CrawlerService crawlerService;

    @Autowired
    private CrawlerFacade crawlerFacade;

    @Test
    void 크롤링_테스트() {

        crawlerService.crawler(UrlGenerator.RIOT_LEADER_BOARD, crawlingPropertiesV1,2801, 2900, 5);
    }

    @Test
    void 분산_크롤링() {
        int[] startPage = {7401, 7451};
        int[] endPage = {7450, 7500};

        crawlerFacade.distributeCrawling(startPage, endPage);
    }
}
