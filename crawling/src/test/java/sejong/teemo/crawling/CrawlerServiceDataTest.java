package sejong.teemo.crawling;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sejong.teemo.crawling.facade.CrawlerFacade;
import sejong.teemo.crawling.property.CrawlingProperties;
import sejong.teemo.crawling.property.CrawlingPropertiesV1;
import sejong.teemo.crawling.property.CrawlingPropertiesV2;
import sejong.teemo.crawling.repository.CrawlerRepository;
import sejong.teemo.crawling.service.CrawlerService;

import java.util.List;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class CrawlerServiceDataTest {

    @Autowired
    private CrawlerRepository crawlerRepository;

    @Autowired
    private CrawlingPropertiesV1 crawlingProperties;

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

        crawlerService.crawler(crawlingProperties,2801, 2900, 5);
    }

    @Test
    void 분산_크롤링() {
        int[] startPage = {4601, 4651};
        int[] endPage = {4650, 4700};

        crawlerFacade.distributeCrawling(startPage, endPage);
    }
}
