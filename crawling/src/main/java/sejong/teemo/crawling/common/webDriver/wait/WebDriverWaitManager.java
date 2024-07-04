package sejong.teemo.crawling.common.webDriver.wait;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebDriverWaitManager {

    private final WebDriverWait wait;

    public WebDriverWaitManager(WebDriverWait wait) {
        this.wait = wait;
    }

    public void untilContentsLoaded(By cssSelector) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(cssSelector));
    }
}
