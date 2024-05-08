package sejong.teemo.crawling.crawler.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import sejong.teemo.crawling.crawler.Crawler;
import sejong.teemo.crawling.domain.Summoner;
import sejong.teemo.crawling.mapper.CrawlerMapperSummoner;
import sejong.teemo.crawling.webDriver.generator.UrlGenerator;

import java.time.Duration;
import java.util.List;

public class LeaderBoardPage implements Page<Summoner> {

    @Override
    public List<Summoner> crawler(WebDriver webDriver, UrlGenerator url, String subUrl) {
        return Crawler.<Summoner>builder()
                .driver(webDriver)
                .driverWait(new WebDriverWait(webDriver, Duration.ofSeconds(20)))
                .urlGenerator(url)
                .build()
                .urlGenerate(urlGenerator -> urlGenerator.generateUrl(subUrl))
                .isWaitingUntilLoadedPage(By.cssSelector("tbody"))
                .actionFromElement(element -> element.findElements(By.cssSelector("tr"))
                        .parallelStream()
                        .map(WebElement::getText)
                        .map(new CrawlerMapperSummoner()::map)
                        .toList()
                );
    }
}
