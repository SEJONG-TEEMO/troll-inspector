package sejong.teemo.crawling.crawler;

import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Crawler<T> {

    default List<T> action(Supplier<List<T>> supplier) { return null; }
    default List<T> action(Function<WebDriver, List<T>> function) { return null; }
    List<T> action();
}
