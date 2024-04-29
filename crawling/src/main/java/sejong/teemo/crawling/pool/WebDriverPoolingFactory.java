package sejong.teemo.crawling.pool;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.DestroyMode;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import sejong.teemo.crawling.property.CrawlingProperties;

import java.net.URI;

public class WebDriverPoolingFactory extends BasePooledObjectFactory<WebDriver> {

    private final CrawlingProperties crawlingProperties;

    public WebDriverPoolingFactory(CrawlingProperties crawlingProperties) {
        this.crawlingProperties = crawlingProperties;
    }

    @Override
    public WebDriver create() {
        FirefoxOptions firefoxOptions = new FirefoxOptions();

        try {
            return new RemoteWebDriver(new URI(crawlingProperties.remoteIp()).toURL(), firefoxOptions);
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
