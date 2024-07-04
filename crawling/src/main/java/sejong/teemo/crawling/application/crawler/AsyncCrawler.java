package sejong.teemo.crawling.application.crawler;

import lombok.Builder;
import org.openqa.selenium.WebDriver;
import sejong.teemo.crawling.common.exception.CrawlingException;
import sejong.teemo.crawling.common.webDriver.pool.WebDriverPool;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

public class AsyncCrawler<T> implements AutoCloseable {

    private final WebDriverPool pool;
    private final ExecutorService executor;

    private int startPage = 0, endPage = 0;

    @Builder
    public AsyncCrawler(WebDriverPool pool, int count) {
        this.pool = pool;
        this.executor = Executors.newFixedThreadPool(count);
    }

    public AsyncCrawler<T> setPages(int startPage, int endPage) {
        this.startPage = startPage;
        this.endPage = endPage;
        return this;
    }

    public AsyncCrawler<T> poolSize(int maxSize) {
        pool.addObjects(maxSize);
        return this;
    }

    public List<List<T>> action(BiFunction<WebDriver, Integer, List<T>> function) {
        List<CompletableFuture<List<T>>> futures = IntStream.rangeClosed(startPage, endPage)
                .mapToObj(pageNum -> CompletableFuture.supplyAsync(() -> {
                    WebDriver webDriver = pool.borrowWebDriver();

                    List<T> list = function.apply(webDriver, pageNum);

                    pool.returnObject(webDriver);
                    return list;
                }, executor).exceptionally(ex -> {
                    throw new CrawlingException(ex.getMessage());
                })).toList();

        return futures.stream().map(CompletableFuture::join).toList();
    }

    @Override
    public void close() throws Exception {
        pool.close();
    }
}
