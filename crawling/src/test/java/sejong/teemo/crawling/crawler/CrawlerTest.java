package sejong.teemo.crawling.crawler;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import sejong.teemo.crawling.dto.InGameDto;
import sejong.teemo.crawling.dto.MatchDataDto;
import sejong.teemo.crawling.exception.CrawlingException;
import sejong.teemo.crawling.mapper.CrawlerMapperInGame;
import sejong.teemo.crawling.mapper.CrawlerMapperMatchData;

import java.time.Duration;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;
import static sejong.teemo.crawling.webDriver.generator.UrlGenerator.*;

class CrawlerTest {

    @Test
    @Disabled
    void 매치_데이터를_크롤링_하고_가져와서_정제하여_리스트를_반환한다() {
        // given
        WebDriver webDriver = new FirefoxDriver();

        String name = "타 잔";
        String tag = "KR1";

        // when
        List<MatchDataDto> dtoList = Crawler.<MatchDataDto>builder()
                .driver(webDriver)
                .driverWait(new WebDriverWait(webDriver, Duration.ofSeconds(10)))
                .baseUrlGenerator(RIOT_SUMMONERS)
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
    @Disabled
    void 인_게임_데이터_크롤링_테스트() {
        // given
        String gameName = "qweasdzxc";
        String tagLine = "1103";
        WebDriver webDriver = new FirefoxDriver();

        // when
        try (Crawler<InGameDto> crawler = new Crawler<>(webDriver, new WebDriverWait(webDriver, Duration.ofSeconds(10)), RIOT_IN_GAME)) {

            List<InGameDto> inGameDtos = crawler.urlGenerate(urlGenerator -> urlGenerator.generateUrl(gameName, tagLine))
                    .isWaitingUntilLoadedPage(By.cssSelector(".css-1m2ho5a"))
                    .actionFromElement(element -> element.findElements(By.cssSelector("table tbody tr"))
                            .parallelStream()
                            .map(WebElement::getText)
                            .map(new CrawlerMapperInGame()::map)
                            .toList());

            // then
            inGameDtos.forEach(System.out::println);
            assertThat(inGameDtos).hasSize(10);

        } catch (Exception e) {
            throw new CrawlingException(e);
        }
    }
}