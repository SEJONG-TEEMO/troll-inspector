package sejong.teemo.crawling.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sejong.teemo.crawling.property.CrawlingProperties;
import sejong.teemo.crawling.service.CrawlerService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class CrawlerFacade {

    private final CrawlerService crawlerService;
    private final List<CrawlingProperties> list;

    public void distributeCrawling(int[] startPage, int[] endPage) {

        List<CompletableFuture<Void>> completableFutures = IntStream.rangeClosed(0, list.size() - 1)
                .mapToObj(i -> CompletableFuture.runAsync(() -> crawlerService.crawler(list.get(i), startPage[i], endPage[i], 3)))
                .toList();

        completableFutures.forEach(CompletableFuture::join);
    }
}
