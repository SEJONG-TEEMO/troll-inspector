package sejong.teemo.crawling.crawler.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import sejong.teemo.crawling.crawler.Crawler;
import sejong.teemo.crawling.dto.MatchDataDto;
import sejong.teemo.crawling.common.mapper.CrawlerMapperMatchData;
import sejong.teemo.crawling.common.generator.UrlGenerator;

import java.time.Duration;
import java.util.List;
import java.util.stream.IntStream;

public class MatchDataPage implements Page<MatchDataDto> {

    @Override
    public List<MatchDataDto> crawler(WebDriver webDriver, UrlGenerator url, String... subUrl) {

        return Crawler.<MatchDataDto>builder()
                .driver(webDriver)
                .driverWait(new WebDriverWait(webDriver, Duration.ofSeconds(10)))
                .baseUrlGenerator(url)
                .build()
                .urlGenerate(urlGenerator -> urlGenerator.generateUrl(subUrl))
                .click(By.cssSelector("li.css-1j5gzz7:nth-child(2)"))
                .isWaitingUntilLoadedPage(By.cssSelector(".css-1jxewmm"))
                .actionFromDriver(driver -> IntStream.rangeClosed(1, 19)
                        .mapToObj(i -> driver.findElement(By.cssSelector("div.css-j7qwjs:nth-child(" + i + ")")))
                        .map(WebElement::getText)
                        .map(new CrawlerMapperMatchData()::map)
                        .toList()
                );
    }
}
