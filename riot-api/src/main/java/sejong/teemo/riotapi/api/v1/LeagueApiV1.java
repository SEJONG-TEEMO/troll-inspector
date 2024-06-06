package sejong.teemo.riotapi.api.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sejong.teemo.riotapi.dto.LeagueEntryDto;
import sejong.teemo.riotapi.dto.SummonerDto;
import sejong.teemo.riotapi.facade.LeagueFacade;
import sejong.teemo.riotapi.service.LeagueService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teemo.gg/api/v1")
public class LeagueApiV1 {

    private final LeagueFacade leagueFacade;
    private final LeagueService leagueService;

    @GetMapping("/league-to-summoner/{division}/{tier}/{queue}")
    public ResponseEntity<List<SummonerDto>> callApiLeagueToSummoner(@PathVariable String division,
                                                                     @PathVariable String tier,
                                                                     @PathVariable String queue,
                                                                     @RequestParam int page) {

        List<SummonerDto> summonerDtos = leagueFacade.callApiLeagueToSummoner(division, tier, queue, page);
        return ResponseEntity.ok(summonerDtos);
    }

    @GetMapping("/league/{division}/{tier}/{queue}")
    public ResponseEntity<List<LeagueEntryDto>> callApiLeague(@PathVariable String division,
                                                              @PathVariable String tier,
                                                              @PathVariable String queue,
                                                              @RequestParam int page) {

        List<LeagueEntryDto> summonerDtos = leagueService.callRiotLeague(division, tier, queue, page);
        return ResponseEntity.ok(summonerDtos);
    }
}
