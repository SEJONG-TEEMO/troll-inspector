package sejong.teemo.crawling.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sejong.teemo.crawling.property.CrawlingProperties;
import sejong.teemo.crawling.service.CrawlerService;
import sejong.teemo.crawling.webDriver.generator.UrlGenerator;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
@Slf4j
public class CrawlerFacade {

    private final CrawlerService crawlerService;
    private final List<CrawlingProperties> list;

    public void distributeCrawling(int[] startPage, int[] endPage) {

        log.info("properties = {}", list);

        List<CompletableFuture<Void>> completableFutures = IntStream.rangeClosed(0, list.size() - 1)
                .mapToObj(i -> CompletableFuture.runAsync(() -> crawlerService.crawler(UrlGenerator.RIOT_LEADER_BOARD, list.get(i), startPage[i], endPage[i], 3)))
                .toList();

        completableFutures.forEach(CompletableFuture::join);
    }
}
