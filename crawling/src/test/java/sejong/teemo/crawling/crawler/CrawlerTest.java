package sejong.teemo.crawling.crawler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.TestPropertySources;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import sejong.teemo.crawling.domain.Summoner;
import sejong.teemo.crawling.dto.MatchDataDto;
import sejong.teemo.crawling.mapper.CrawlerMapperMatchData;
import sejong.teemo.crawling.mapper.CrawlerMapperSummoner;
import sejong.teemo.crawling.property.CrawlingPropertiesV1;
import sejong.teemo.crawling.util.ParserUtil;
import sejong.teemo.crawling.webDriver.generator.UrlGenerator;

import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

class CrawlerTest {

    @Test
    void 매치_데이터를_크롤링_하고_가져와서_정제하여_리스트를_반환한다() {
        // given
        WebDriver webDriver = new FirefoxDriver();

        String name = "타 잔";
        String tag = "KR1";

        // when
        List<MatchDataDto> dtoList = PageCrawler.<MatchDataDto>builder()
                .driver(webDriver)
                .driverWait(new WebDriverWait(webDriver, Duration.ofSeconds(10)))
                .urlGenerator(UrlGenerator.RIOT_SUMMONERS)
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
        List<Summoner> summoners = PageCrawler.<Summoner>builder()
                .driver(webDriver)
                .driverWait(new WebDriverWait(webDriver, Duration.ofSeconds(20)))
                .urlGenerator(UrlGenerator.RIOT_LEADER_BOARD)
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
}