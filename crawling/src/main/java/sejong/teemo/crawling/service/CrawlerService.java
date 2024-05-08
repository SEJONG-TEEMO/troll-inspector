package sejong.teemo.crawling.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sejong.teemo.crawling.crawler.Crawler;
import sejong.teemo.crawling.crawler.page.LeaderBoardPage;
import sejong.teemo.crawling.crawler.page.Page;
import sejong.teemo.crawling.crawler.page.PageImpl;
import sejong.teemo.crawling.crawler.page.SummonerPage;
import sejong.teemo.crawling.domain.Summoner;
import sejong.teemo.crawling.dto.MatchDataDto;
import sejong.teemo.crawling.exception.CrawlingException;
import sejong.teemo.crawling.mapper.CrawlerMapperMatchData;
import sejong.teemo.crawling.mapper.CrawlerMapperSummoner;
import sejong.teemo.crawling.webDriver.generator.UrlGenerator;
import sejong.teemo.crawling.webDriver.pool.WebDriverPool;
import sejong.teemo.crawling.webDriver.pool.WebDriverPoolingFactory;
import sejong.teemo.crawling.property.CrawlingProperties;
import sejong.teemo.crawling.repository.CrawlerRepository;

import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class CrawlerService {

    private final CrawlerRepository crawlerRepository;

    @Transactional
    public void crawler(UrlGenerator url, CrawlingProperties properties, int startPage, int endPage, int maxPoolSize) {
        log.info("url = {}", url);
        
        WebDriverPool webDriverPool = new WebDriverPool(new WebDriverPoolingFactory(properties), maxPoolSize);

        webDriverPool.addObjects(maxPoolSize);

        ExecutorService executorService = Executors.newFixedThreadPool(maxPoolSize);
        List<CompletableFuture<List<Summoner>>> futures = IntStream.rangeClosed(startPage, endPage)
                .mapToObj(pageNum -> CompletableFuture.supplyAsync(() -> {
                    log.info("page = {}", pageNum);
                    WebDriver webDriver = webDriverPool.borrowWebDriver();

                    Page<Summoner> page = new LeaderBoardPage();
                    List<Summoner> list = page.crawler(webDriver, url, String.valueOf(pageNum));

                    webDriverPool.returnObject(webDriver);
                    return list;
                }, executorService).exceptionally(ex -> {
                    throw new CrawlingException(ex.getMessage());
                })).toList();

        insertCrawlingData(futures);

        executorService.shutdown();
        webDriverPool.close();
    }

    private void insertCrawlingData(List<CompletableFuture<List<Summoner>>> futures) {
        futures.stream()
                .map(CompletableFuture::join)
                .forEach(crawlerRepository::bulkInsert);
    }

    public List<MatchDataDto> crawlingMatchData(UrlGenerator url, CrawlingProperties properties, String name, String tag) {

        try {
            WebDriver webDriver = new RemoteWebDriver(new URI(properties.remoteIp()).toURL(), new FirefoxOptions());

            Page<MatchDataDto> page = new SummonerPage();
            List<MatchDataDto> list = page.crawler(webDriver, url, name + "-" + tag);

            webDriver.close();
            return list;
        } catch (Exception e) {
            throw new CrawlingException(e.getMessage());
        }
    }
}
