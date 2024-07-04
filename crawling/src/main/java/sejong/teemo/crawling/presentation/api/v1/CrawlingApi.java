package sejong.teemo.crawling.presentation.api.v1;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sejong.teemo.crawling.presentation.dto.InGameDto;
import sejong.teemo.crawling.presentation.dto.MatchDataDto;
import sejong.teemo.crawling.common.property.CrawlingProperties;
import sejong.teemo.crawling.application.service.CrawlerService;
import sejong.teemo.crawling.common.generator.UrlGenerator;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CrawlingApi {

    private final CrawlerService crawlerService;
    private final CrawlingProperties crawlingProperties;

    public CrawlingApi(CrawlerService crawlerService,
                       @Qualifier("crawling-v1-sejong.teemo.crawling.property.CrawlingPropertiesV1") CrawlingProperties crawlingProperties) {
        this.crawlerService = crawlerService;
        this.crawlingProperties = crawlingProperties;
    }

    @GetMapping("crawling/{summoner-name}/{tag}/match")
    ResponseEntity<List<MatchDataDto>> apiCrawlMatchData(@PathVariable("summoner-name") String summonerName,
                                                         @PathVariable("tag") String tag) {

        List<MatchDataDto> matchDataDtos = crawlerService.crawlingMatchData(UrlGenerator.RIOT_SUMMONERS, crawlingProperties, summonerName, tag);

        return ResponseEntity.ok(matchDataDtos);
    }

    @GetMapping("crawling/{summoner-name}/{tag}/ingame")
    ResponseEntity<List<InGameDto>> apiCrawlingInGame(@PathVariable("summoner-name") String summonerName,
                                                      @PathVariable("tag") String tag) {

        List<InGameDto> inGameDtos = crawlerService.crawlingInGame(UrlGenerator.RIOT_IN_GAME, crawlingProperties, summonerName, tag);

        return ResponseEntity.ok(inGameDtos);
    }
}
