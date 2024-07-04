package sejong.teemo.crawling.application.crawler;

import lombok.Builder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import sejong.teemo.crawling.common.generator.UrlGenerator;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class Crawler<T> implements AutoCloseable {

    private final WebDriver driver;
    private final WebDriverWait driverWait;
    private final UrlGenerator baseUrlGenerator;

    private WebElement webElement;

    @Builder
    public Crawler(
            WebDriver driver,
            WebDriverWait driverWait,
            UrlGenerator baseUrlGenerator
    ) {
        Objects.requireNonNull(driver);
        Objects.requireNonNull(driverWait);
        Objects.requireNonNull(baseUrlGenerator);

        this.driver = driver;
        this.driverWait = driverWait;
        this.baseUrlGenerator = baseUrlGenerator;
    }

    public Crawler<T> urlGenerate(Function<UrlGenerator, String> function) {
        String url = function.apply(baseUrlGenerator);
        driver.get(url);
        return this;
    }

    public Crawler<T> click(By cssSelector) {
        driver.findElement(cssSelector).click();
        return this;
    }

    public Crawler<T> isWaitingUntilLoadedPage(By cssSelector) {
        webElement = driverWait.until(ExpectedConditions.visibilityOfElementLocated(cssSelector));
        return this;
    }

    public List<T> actionFromDriver(Function<WebDriver, List<T>> function) {
        return function.apply(driver);
    }

    public List<T> actionFromElement(Function<WebElement, List<T>> function) {
        return function.apply(webElement);
    }

    public List<T> action() {
        return null;
    }

    @Override
    public void close() {
        driver.quit();
    }
}
