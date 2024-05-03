package sejong.teemo.crawling.api.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sejong.teemo.crawling.service.CrawlerService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class CrawlingApi {

    private final CrawlerService crawlerService;


}
