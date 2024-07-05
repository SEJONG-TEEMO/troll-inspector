package sejong.teemo.riotapi.presentation.api.v1;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sejong.teemo.riotapi.application.service.MatchService;
import sejong.teemo.riotapi.domain.match.dto.MatchDataDto;
import sejong.teemo.riotapi.domain.match.dto.MatchDto;
import sejong.teemo.riotapi.domain.summoner.dto.SummonerPerformance;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/teemo.gg/api/v1")
public class MatchApiV1 {

    private final MatchService matchService;

    @GetMapping("/match/{gameName}/{tagLine}")
    public ResponseEntity<List<MatchDto>> callApiMatch(@PathVariable("gameName") String gameName,
                                                       @PathVariable("tagLine") String tagLine) {

        return ResponseEntity.ok(matchService.callRiotMatch(gameName, tagLine));
    }

    @GetMapping("/match/summoner-performance/{gameName}/{tagLine}")
    public ResponseEntity<List<SummonerPerformance>> callApiMatchSummonerPerformance(
            @PathVariable("gameName") String gameName,
            @PathVariable("tagLine") String tagLine) {

        return ResponseEntity.ok(matchService.callRiotSummonerPerformance(gameName, tagLine));
    }

    @GetMapping("/match/summoner-performance/{puuid}")
    public ResponseEntity<List<SummonerPerformance>> callApiMatchSummonerPerformance(
            @PathVariable("puuid") String puuid) {
        return ResponseEntity.ok(matchService.callRiotSummonerPerformance(puuid));
    }

    @GetMapping("/match/{puuid}")
    public ResponseEntity<List<MatchDataDto>> callApiMatch(@PathVariable("puuid") String puuid) {
        return ResponseEntity.ok(matchService.callRiotMatch(puuid));
    }
}
