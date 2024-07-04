package sejong.teemo.crawling.application.crawler.page;

import sejong.teemo.crawling.common.generator.UrlGenerator;
import sejong.teemo.crawling.common.webDriver.pool.WebDriverPool;

import java.util.List;
import java.util.concurrent.ExecutorService;

public interface Pages<T> {

    List<List<T>> asyncCrawler(UrlGenerator url, WebDriverPool pool, ExecutorService executorService, int startPage, int endPage, int maxSize);
}
