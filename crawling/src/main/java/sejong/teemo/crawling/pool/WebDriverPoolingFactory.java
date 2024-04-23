package sejong.teemo.crawling.pool;

import lombok.RequiredArgsConstructor;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.DestroyMode;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.stereotype.Component;
import sejong.teemo.crawling.property.CrawlingProperties;

import java.net.URL;

@Component
@RequiredArgsConstructor
public class WebDriverPoolingFactory extends BasePooledObjectFactory<WebDriver> {

    private final FirefoxOptions options;
    private final CrawlingProperties crawlingProperties;

    @Override
    public WebDriver create() {
        try {
            return new RemoteWebDriver(new URL(crawlingProperties.remoteIp()), options);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PooledObject<WebDriver> wrap(WebDriver webDriver) {
        return new DefaultPooledObject<>(webDriver);
    }

    @Override
    public void destroyObject(PooledObject<WebDriver> p, DestroyMode destroyMode) throws Exception {
        p.getObject().quit();
    }
}
