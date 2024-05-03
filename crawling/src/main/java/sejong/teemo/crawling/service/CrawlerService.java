package sejong.teemo.crawling.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sejong.teemo.crawling.domain.Summoner;
import sejong.teemo.crawling.dto.MatchDataDto;
import sejong.teemo.crawling.pool.WebDriverPool;
import sejong.teemo.crawling.pool.WebDriverPoolingFactory;
import sejong.teemo.crawling.property.CrawlingProperties;
import sejong.teemo.crawling.property.CrawlingPropertiesV1;
import sejong.teemo.crawling.repository.CrawlerRepository;
import sejong.teemo.crawling.util.ParserUtil;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CrawlerService {

    private final CrawlerRepository crawlerRepository;

    @Transactional
    public void crawler(CrawlingProperties properties, int startPage, int endPage, int maxPoolSize) {
        log.info("url = {}", properties.url());
        
        WebDriverPool webDriverPool = new WebDriverPool(new WebDriverPoolingFactory(properties), maxPoolSize);

        webDriverPool.addObjects(maxPoolSize);

        ExecutorService executorService = Executors.newFixedThreadPool(maxPoolSize);
        List<CompletableFuture<Object>> futures = IntStream.rangeClosed(startPage, endPage)
                .mapToObj(page -> CompletableFuture.supplyAsync(() -> {
                    log.info("page = {}", page);
                    WebDriver webDriver = webDriverPool.borrowWebDriver();

                    webDriver.get(properties.url() + page);

                    // 랭킹 정보가 표시된 테이블 요소 선택 (XPath 또는 CSS Selector 사용)
                    WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(20));
                    WebElement rankingTable = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("tbody")));

                    // 테이블에서 각 플레이어의 랭킹 정보 추출
                    List<Summoner> summoners = rankingTable.findElements(By.cssSelector("tr"))
                            .parallelStream()
                            .map(WebElement::getText)
                            .map(ParserUtil::parseSummoner)
                            .toList();

                    webDriverPool.returnObject(webDriver);
                    return summoners;
                }, executorService).exceptionally(ex -> {
                    throw new RuntimeException(ex);
                }).thenApply(summoners -> {
                    crawlerRepository.bulkInsert(summoners);
                    return null;
                })).toList();

        List<Object> result = futures.parallelStream()
                .map(CompletableFuture::join)
                .toList();

        log.info("result = {}", result.size());

        webDriverPool.clear();
        webDriverPool.close();
    }

    public List<MatchDataDto> crawlingMatchData(CrawlingProperties properties, String name, String tag) {

        try {
            WebDriver webDriver = new RemoteWebDriver(new URI(properties.remoteIp()).toURL(), new FirefoxOptions());

            webDriver.get(properties.url() + name + ParserUtil.skipString("#", tag));

            webDriver.findElement(By.cssSelector("li.css-1j5gzz7:nth-child(2)")).click();

            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.css-j7qwjs:nth-child(1)")));

            return IntStream.rangeClosed(1, 19)
                    .parallel()
                    .mapToObj(i -> webDriver.findElement(By.cssSelector("div.css-j7qwjs:nth-child(" + i + ")")))
                    .map(WebElement::getText)
                    .map(ParserUtil::parseMatchData)
                    .toList();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
