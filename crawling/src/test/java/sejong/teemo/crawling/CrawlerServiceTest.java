package sejong.teemo.crawling;

import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import sejong.teemo.crawling.facade.CrawlerFacade;
import sejong.teemo.crawling.property.CrawlingProperties;
import sejong.teemo.crawling.property.CrawlingPropertiesV1;
import sejong.teemo.crawling.property.CrawlingPropertiesV2;
import sejong.teemo.crawling.repository.CrawlerRepository;
import sejong.teemo.crawling.service.CrawlerService;

import java.util.List;

class CrawlerServiceTest extends TestContainer {

    @Autowired
    private CrawlerRepository crawlerRepository;

    @Autowired
    private CrawlingPropertiesV1 crawlingPropertiesV1;

    @Autowired
    private CrawlingPropertiesV2 crawlingPropertiesV2;

    @Autowired
    private List<CrawlingProperties> list;

    @Autowired
    private CrawlerService crawlerService;

    @Autowired
    private CrawlerFacade crawlerFacade;

    @Test
    void 크롤링_테스트() {

        crawlerService.crawler(crawlingPropertiesV1,1, 100, 5);
    }

    @Test
    void 분산_크롤링_테스트() {
        // given
        int[] startPage = {1, 51};
        int[] endPage = {50, 100};

        // when
        crawlerFacade.distributeCrawling(startPage, endPage);

        // then

    }
}