package sejong.teemo.riotapi.api.v1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sejong.teemo.riotapi.dto.SummonerPerformance;
import sejong.teemo.riotapi.dto.match.MatchDataDto;
import sejong.teemo.riotapi.dto.match.MatchDto;
import sejong.teemo.riotapi.facade.MatchFacade;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/teemo.gg/api/v1")
public class MatchApiV1 {

    private final MatchFacade matchFacade;

    @GetMapping("/match/{gameName}/{tagLine}")
    public ResponseEntity<List<MatchDto>> callApiMatch(@PathVariable("gameName") String gameName,
                                                       @PathVariable("tagLine") String tagLine) {

        return ResponseEntity.ok(matchFacade.callRiotMatch(gameName, tagLine));
    }

    @GetMapping("/match/summoner-performance/{gameName}/{tagLine}")
    public ResponseEntity<List<SummonerPerformance>> callApiMatchSummonerPerformance(@PathVariable("gameName") String gameName,
                                                                                     @PathVariable("tagLine") String tagLine) {

        return ResponseEntity.ok(matchFacade.callRiotSummonerPerformance(gameName, tagLine));
    }

    @GetMapping("/match/summoner-performance/{puuid}")
    public ResponseEntity<List<SummonerPerformance>> callApiMatchSummonerPerformance(@PathVariable("puuid") String puuid) {
        return ResponseEntity.ok(matchFacade.callRiotSummonerPerformance(puuid));
    }

    @GetMapping("/match/{puuid}")
    public ResponseEntity<List<MatchDataDto>> callApiMatch(@PathVariable String puuid) {
        return ResponseEntity.ok(matchFacade.callRiotMatch(puuid));
    }
}
