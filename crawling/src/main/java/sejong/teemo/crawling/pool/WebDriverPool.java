package sejong.teemo.crawling.pool;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.openqa.selenium.WebDriver;

import java.util.HashSet;
import java.util.Set;

public class WebDriverPool extends GenericObjectPool<WebDriver> {

    private final Set<WebDriver> webDriverObjects = new HashSet<>();

    public WebDriverPool(PooledObjectFactory<WebDriver> factory) {
        super(factory);
    }

    public WebDriverPool(PooledObjectFactory<WebDriver> factory, int maxPoolSize) {
        super(factory);
        this.setMaxTotal(maxPoolSize);
    }

    @Override
    public void addObjects(int count) {
        try {
            super.addObjects(count);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public WebDriver borrowWebDriver() {
        try {
            WebDriver webDriver = this.borrowObject();
            webDriverObjects.add(webDriver);
            return webDriver;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void returnObject(WebDriver webDriver) {
        webDriverObjects.remove(webDriver);
        super.returnObject(webDriver);
    }

    @Override
    public void close() {
        webDriverObjects.forEach(this::returnObject);
        super.close();
    }

    @Override
    public void clear() {
        webDriverObjects.forEach(this::returnObject);
        super.clear();
    }
}
