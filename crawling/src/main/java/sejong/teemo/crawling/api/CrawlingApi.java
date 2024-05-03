package sejong.teemo.crawling.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import sejong.teemo.crawling.service.CrawlerService;

@RestController
@RequiredArgsConstructor
public class CrawlingApi {

    private final CrawlerService crawlerService;


}
