package sejong.teemo.crawling;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import sejong.teemo.crawling.service.CrawlerService;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CrawlerServiceTest {

    private static final String url = "https://www.op.gg/leaderboards/tier?page=";

    @Test
    void 크롤링_테스트() {
        CrawlerService crawlerService = new CrawlerService(url);

        crawlerService.crawler(1, 100, 5);
    }
}