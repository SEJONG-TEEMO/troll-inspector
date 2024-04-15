package sejong.teemo.crawling.pool;

import lombok.RequiredArgsConstructor;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.DestroyMode;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

@RequiredArgsConstructor
public class WebDriverPoolingFactory extends BasePooledObjectFactory<WebDriver> {

    private final FirefoxOptions options;

    @Override
    public WebDriver create() {
        return new FirefoxDriver(options);
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
