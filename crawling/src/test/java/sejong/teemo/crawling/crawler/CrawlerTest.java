package sejong.teemo.crawling.crawler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import sejong.teemo.crawling.property.CrawlingPropertiesV1;
import sejong.teemo.crawling.property.CrawlingPropertiesV2;

@ContextConfiguration(classes = {
        CrawlingPropertiesV1.class,
        CrawlingPropertiesV2.class
})
class CrawlerTest {

    @Autowired
    private CrawlingPropertiesV1 propertiesV1;

    @Test
    void 크롤러_테스트_메서드_체이닝() {
        PagesCrawler pagesCrawler = new PagesCrawler(propertiesV1);

    }
}