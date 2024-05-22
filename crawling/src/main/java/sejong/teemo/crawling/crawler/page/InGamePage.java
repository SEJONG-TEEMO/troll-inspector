package sejong.teemo.crawling.crawler.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import sejong.teemo.crawling.crawler.Crawler;
import sejong.teemo.crawling.dto.InGameDto;
import sejong.teemo.crawling.exception.CrawlingException;
import sejong.teemo.crawling.mapper.CrawlerMapperInGame;
import sejong.teemo.crawling.webDriver.generator.UrlGenerator;

import java.time.Duration;
import java.util.List;

import static sejong.teemo.crawling.webDriver.generator.UrlGenerator.RIOT_IN_GAME;

public class InGamePage implements Page<InGameDto> {

    @Override
    public List<InGameDto> crawler(WebDriver webDriver, UrlGenerator url, String... subUrl) {

        try (Crawler<InGameDto> crawler = new Crawler<>(webDriver, new WebDriverWait(webDriver, Duration.ofSeconds(10)), url)) {

            List<InGameDto> inGameDtos = crawler.urlGenerate(urlGenerator -> urlGenerator.generateUrl(subUrl))
                    .isWaitingUntilLoadedPage(By.cssSelector("table"))
                    .actionFromElement(element -> element.findElements(By.cssSelector("table tbody tr"))
                            .parallelStream()
                            .map(WebElement::getText)
                            .map(new CrawlerMapperInGame()::map)
                            .toList());

            webDriver.close();

            return inGameDtos;

        } catch (Exception e) {
            throw new CrawlingException(e);
        }
    }
}