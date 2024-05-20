package sejong.teemo.crawling.crawler.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import sejong.teemo.crawling.crawler.AsyncCrawler;
import sejong.teemo.crawling.crawler.Crawler;
import sejong.teemo.crawling.domain.Summoner;
import sejong.teemo.crawling.mapper.CrawlerMapperSummoner;
import sejong.teemo.crawling.webDriver.generator.UrlGenerator;
import sejong.teemo.crawling.webDriver.pool.WebDriverPool;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutorService;

import static sejong.teemo.crawling.webDriver.generator.UrlGenerator.RIOT_LEADER_BOARD;

public class LeaderBoardPage implements Page<Summoner>, Pages<Summoner> {

    @Override
    public List<Summoner> crawler(WebDriver webDriver, UrlGenerator url, String... subUrl) {
        return Crawler.<Summoner>builder()
                .driver(webDriver)
                .driverWait(new WebDriverWait(webDriver, Duration.ofSeconds(20)))
                .baseUrlGenerator(url)
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

    @Override
    public List<List<Summoner>> asyncCrawler(UrlGenerator url, WebDriverPool pool, ExecutorService executorService, int startPage, int endPage, int maxSize) {
        return AsyncCrawler.<Summoner>builder()
                .pool(pool)
                .executor(executorService)
                .build()
                .setPages(startPage, endPage)
                .poolSize(maxSize)
                .action((webDriver, pageNum) -> new LeaderBoardPage().crawler(webDriver, RIOT_LEADER_BOARD, String.valueOf(pageNum)));
    }
}
