package sejong.teemo.crawling.crawler.page;

import org.openqa.selenium.WebDriver;
import sejong.teemo.crawling.webDriver.generator.UrlGenerator;

import java.util.List;

public interface Page<T> {

    List<T> crawler(WebDriver webDriver, UrlGenerator url, String... subUrl);
}
