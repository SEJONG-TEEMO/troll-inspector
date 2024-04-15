package sejong.teemo.crawling.service;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sejong.teemo.crawling.domain.Summoner;
import sejong.teemo.crawling.pool.WebDriverPool;
import sejong.teemo.crawling.pool.WebDriverPoolingFactory;
import sejong.teemo.crawling.util.ParserUtil;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

@Service
@Slf4j
public class CrawlerService {

    private static final int MAX_PAGE = 28712;
    private final String url;

    @Builder
    public CrawlerService(@Value("web.url") String url) {
        this.url = url;
    }

    public void crawler() {
        log.info("url = {}", url);

        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--disable-popup-blocking");       //팝업안띄움
        //options.addArguments("--headless");                       //브라우저 안띄움
        options.addArguments("--disable-gpu");			//gpu 비활성화
        options.addArguments("--blink-settings=imagesEnabled=false"); //이미지 다운 안받음

        WebDriverPool webDriverPool = new WebDriverPool(new WebDriverPoolingFactory(options), 5);

        webDriverPool.addObjects(5);

        ExecutorService executorService = Executors.newFixedThreadPool(64);
        List<CompletableFuture<List<Summoner>>> futures = IntStream.rangeClosed(1, 100)
                .mapToObj(page -> CompletableFuture.supplyAsync(() -> {
                    log.info("webDriver borrow");
                    WebDriver webDriver = webDriverPool.borrowWebDriver();
                    log.info("webDriver");

                    webDriver.get(url + page);

                    // 랭킹 정보가 표시된 테이블 요소 선택 (XPath 또는 CSS Selector 사용)
                    //WebElement rankingTable = webDriver.findElement(By.cssSelector(".css-1l95r9q > tbody"));
                    WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
                    WebElement rankingTable = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".css-1l95r9q > tbody")));

                    // 테이블에서 각 플레이어의 랭킹 정보 추출
                    List<Summoner> summoners = rankingTable.findElements(By.cssSelector("tr"))
                            .parallelStream()
                            .map(WebElement::getText)
                            .map(ParserUtil::parseSummoner)
                            .toList();

                    webDriverPool.returnObject(webDriver);
                    return summoners;
                }, executorService)).toList();

        List<List<Summoner>> result = futures.parallelStream()
                .map(CompletableFuture::join)
                .toList();

        result.forEach(summoners -> {
            log.info("summoners = {}", summoners);
        });

        webDriverPool.clear();
        webDriverPool.close();
    }
}
