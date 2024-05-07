package sejong.teemo.crawling.crawler;

import lombok.Builder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import sejong.teemo.crawling.mapper.CrawlerMapper;
import sejong.teemo.crawling.webDriver.generator.UrlGenerator;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class PageCrawler<T> implements Crawler<T> {

    private final WebDriver driver;
    private final WebDriverWait driverWait;
    private final UrlGenerator urlGenerator;
    private final CrawlerMapper<T> crawlerMapper;

    @Builder
    public PageCrawler(
            WebDriver driver,
            WebDriverWait driverWait,
            UrlGenerator urlGenerator,
            CrawlerMapper<T> crawlerMapper
    ) {
        Objects.requireNonNull(driver);
        Objects.requireNonNull(driverWait);
        Objects.requireNonNull(urlGenerator);
        Objects.requireNonNull(crawlerMapper);

        this.driver = driver;
        this.driverWait = driverWait;
        this.urlGenerator = urlGenerator;
        this.crawlerMapper = crawlerMapper;
    }

    public PageCrawler<T> urlGenerate(Function<UrlGenerator, String> function) {
        String url = function.apply(urlGenerator);
        driver.get(url);
        return this;
    }

    //By.cssSelector(".css-1jxewmm")
    public PageCrawler<T> click(By cssSelector) {
        driver.findElement(cssSelector).click();
        return this;
    }

    public PageCrawler<T> isWaitingUntilLoadedPage(By cssSelector) {
        driverWait.until(ExpectedConditions.visibilityOfElementLocated(cssSelector));
        return this;
    }

    @Override
    public List<T> action(Function<WebDriver, List<T>> function) {
        return function.apply(driver);
    }

    @Override
    public List<T> action() {

        return IntStream.rangeClosed(1, 19)
                .parallel()
                .mapToObj(i -> driver.findElement(By.cssSelector("div.css-j7qwjs:nth-child(" + i + ")")))
                .map(WebElement::getText)
                .map(crawlerMapper::map)
                .toList();
    }
}
