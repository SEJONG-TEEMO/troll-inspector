package sejong.teemo.crawling.service;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.VirtualThreadTaskExecutor;
import org.springframework.stereotype.Component;
import sejong.teemo.crawling.domain.Summoner;
import sejong.teemo.crawling.util.ParserUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
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
        options.addArguments("--headless");                       //브라우저 안띄움
        options.addArguments("--disable-gpu");			//gpu 비활성화
        options.addArguments("--blink-settings=imagesEnabled=false"); //이미지 다운 안받음

        List<CompletableFuture<List<Summoner>>> futures = IntStream.rangeClosed(1, 3)
                .mapToObj(page -> CompletableFuture.supplyAsync(() -> {
                    WebDriver webDriver = new FirefoxDriver(options);
                    webDriver.get(url + page);

                    // 랭킹 정보가 표시된 테이블 요소 선택 (XPath 또는 CSS Selector 사용)
                    WebElement rankingTable = webDriver.findElement(By.cssSelector(".css-1l95r9q > tbody"));

                    // 테이블에서 각 플레이어의 랭킹 정보 추출
                    List<Summoner> summoners = rankingTable.findElements(By.cssSelector("tr"))
                            .parallelStream()
                            .map(WebElement::getText)
                            .map(ParserUtil::parseSummoner)
                            .toList();

                    webDriver.quit();
                    return summoners;
                })).toList();

        // 모든 비동기 작업이 완료될 때까지 대기
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenAccept(v -> {
                    // 모든 결과를 수집
                    List<List<Summoner>> result = futures.stream()
                            .map(CompletableFuture::join)
                            .toList();

                    // 결과 처리
                    result.forEach(summoners -> {
                        log.info("summoners = {}", summoners);
                    });
                }).join(); // 대기

//        for(int page = 1; page <= 3; page++) {
//            WebDriver webDriver = new FirefoxDriver(options);
//            webDriver.get(url + page);
//
//            // 랭킹 정보가 표시된 테이블 요소 선택 (XPath 또는 CSS Selector 사용)
//            WebElement rankingTable = webDriver.findElement(By.cssSelector(".css-1l95r9q > tbody"));
//
//            // 테이블에서 각 플레이어의 랭킹 정보 추출
//            List<Summoner> summoners = rankingTable.findElements(By.cssSelector("tr"))
//                    .parallelStream()
//                    .map(WebElement::getText)
//                    .map(ParserUtil::parseSummoner)
//                    .toList();
//
//            log.info("summoners = {}", summoners);
//        }
    }
}
