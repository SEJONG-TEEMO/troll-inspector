package sejong.teemo.crawling.crawler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import sejong.teemo.crawling.CrawlingApplication;
import sejong.teemo.crawling.container.TestContainer;
import sejong.teemo.crawling.crawler.page.LeaderBoardPage;
import sejong.teemo.crawling.domain.Summoner;
import sejong.teemo.crawling.dto.MatchDataDto;
import sejong.teemo.crawling.mapper.CrawlerMapperMatchData;
import sejong.teemo.crawling.mapper.CrawlerMapperSummoner;
import sejong.teemo.crawling.property.CrawlingProperties;
import sejong.teemo.crawling.property.CrawlingPropertiesV1;
import sejong.teemo.crawling.webDriver.pool.WebDriverPool;
import sejong.teemo.crawling.webDriver.pool.WebDriverPoolingFactory;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;
import static sejong.teemo.crawling.webDriver.generator.UrlGenerator.*;

class CrawlerTest extends TestContainer {

    @Autowired
    private CrawlingPropertiesV1 crawlingPropertiesV1;

    @Test
    void 매치_데이터를_크롤링_하고_가져와서_정제하여_리스트를_반환한다() {
        // given
        WebDriver webDriver = new FirefoxDriver();

        String name = "타 잔";
        String tag = "KR1";

        // when
        List<MatchDataDto> dtoList = Crawler.<MatchDataDto>builder()
                .driver(webDriver)
                .driverWait(new WebDriverWait(webDriver, Duration.ofSeconds(10)))
                .urlGenerator(RIOT_SUMMONERS)
                .build()
                .urlGenerate(urlGenerator -> urlGenerator.generateUrl(name + "-" + tag))
                .click(By.cssSelector("li.css-1j5gzz7:nth-child(2)"))
                .isWaitingUntilLoadedPage(By.cssSelector(".css-1jxewmm"))
                .actionFromDriver(driver -> IntStream.rangeClosed(1, 19)
                        .mapToObj(i -> driver.findElement(By.cssSelector("div.css-j7qwjs:nth-child(" + i + ")")))
                        .map(WebElement::getText)
                        .map(new CrawlerMapperMatchData()::map)
                        .toList()
                );

        // then
        webDriver.close();
        assertThat(dtoList.size()).isEqualTo(19);
    }

    @Test
    void 순위_정보를_크롤링_하고_가져와서_정제하여_리스트를_반환한다() {
        // given
        WebDriver webDriver = new FirefoxDriver();

        // when
        List<Summoner> summoners = Crawler.<Summoner>builder()
                .driver(webDriver)
                .driverWait(new WebDriverWait(webDriver, Duration.ofSeconds(20)))
                .urlGenerator(RIOT_LEADER_BOARD)
                .build()
                .urlGenerate(urlGenerator -> urlGenerator.generateUrl(1))
                .isWaitingUntilLoadedPage(By.cssSelector("tbody"))
                .actionFromElement(element -> element.findElements(By.cssSelector("tr"))
                        .parallelStream()
                        .map(WebElement::getText)
                        .map(new CrawlerMapperSummoner()::map)
                        .toList()
                );

        // then
        webDriver.close();
        assertThat(summoners).hasSize(100);
    }

    @Test
    void 여러개의_순위_정보를_비동기_크롤링_하고_가져와서_정제하여_리스트를_반환한다() {
        // given
        int maxPoolSize = 2;
        int startPage = 1, endPage = 5;
        WebDriverPool webDriverPool = new WebDriverPool(new WebDriverPoolingFactory(crawlingPropertiesV1), maxPoolSize);
        ExecutorService executorService = Executors.newFixedThreadPool(maxPoolSize);

        // when
        List<List<Summoner>> list = AsyncCrawler.<Summoner>builder()
                .pool(webDriverPool)
                .executor(executorService)
                .build()
                .setPages(startPage, endPage)
                .poolSize(maxPoolSize)
                .action((webDriver, pageNum) -> new LeaderBoardPage().crawler(webDriver, RIOT_LEADER_BOARD, String.valueOf(pageNum)));

        executorService.shutdown();
        webDriverPool.close();

        // then
        assertThat(list).hasSize(5);
    }
}