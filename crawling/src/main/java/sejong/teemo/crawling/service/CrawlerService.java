package sejong.teemo.crawling.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sejong.teemo.crawling.crawler.page.*;
import sejong.teemo.crawling.domain.Summoner;
import sejong.teemo.crawling.dto.InGameDto;
import sejong.teemo.crawling.dto.MatchDataDto;
import sejong.teemo.crawling.exception.CrawlingException;
import sejong.teemo.crawling.webDriver.generator.UrlGenerator;
import sejong.teemo.crawling.webDriver.pool.WebDriverPool;
import sejong.teemo.crawling.webDriver.pool.WebDriverPoolingFactory;
import sejong.teemo.crawling.property.CrawlingProperties;
import sejong.teemo.crawling.repository.CrawlerRepository;

import java.net.URI;
import java.util.List;
import java.util.concurrent.*;

import static sejong.teemo.crawling.webDriver.generator.UrlGenerator.RIOT_LEADER_BOARD;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CrawlerService {

    private final CrawlerRepository crawlerRepository;

    @Transactional
    public void crawler(UrlGenerator url, CrawlingProperties properties, int startPage, int endPage, int maxPoolSize) {
        log.info("url = {}", url);

        try (WebDriverPool webDriverPool = new WebDriverPool(new WebDriverPoolingFactory(properties), maxPoolSize)) {
            ExecutorService executorService = Executors.newFixedThreadPool(maxPoolSize);

            Pages<Summoner> pages = new LeaderBoardPage();

            List<List<Summoner>> lists = pages.asyncCrawler(url, webDriverPool, executorService, startPage, endPage, maxPoolSize);
            insertCrawlingData(lists);

            executorService.shutdown();
        } catch (Exception e) {
            throw new CrawlingException(e.getMessage());
        }
    }

    private void insertCrawlingData(List<List<Summoner>> summonerList) {
        summonerList.forEach(crawlerRepository::bulkInsert);
    }

    public List<MatchDataDto> crawlingMatchData(UrlGenerator url, CrawlingProperties properties, String name, String tag) {

        try {
            WebDriver webDriver = new RemoteWebDriver(new URI(properties.remoteIp()).toURL(), new FirefoxOptions());

            Page<MatchDataDto> page = new MatchDataPage();
            List<MatchDataDto> list = page.crawler(webDriver, url, name + "-" + tag);

            webDriver.close();
            return list;
        } catch (Exception e) {
            throw new CrawlingException(e.getMessage());
        }
    }

    public List<InGameDto> crawlingInGame(UrlGenerator url, CrawlingProperties properties, String name, String tag) {

        Page<InGameDto> page = new InGamePage();

        return page.crawler(new FirefoxDriver(), url, name, tag);
    }
}
