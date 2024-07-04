package sejong.teemo.crawling.application.crawler.page;

import org.openqa.selenium.WebDriver;
import sejong.teemo.crawling.common.generator.UrlGenerator;

import java.util.List;

public interface Page<T> {

    List<T> crawler(WebDriver webDriver, UrlGenerator url, String... subUrl);
    default List<T> crawler(String... subUrl) {
        return null;
    }
}
