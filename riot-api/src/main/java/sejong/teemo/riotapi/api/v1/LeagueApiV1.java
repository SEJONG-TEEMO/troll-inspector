package sejong.teemo.riotapi.api.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sejong.teemo.riotapi.dto.LeagueEntryDto;
import sejong.teemo.riotapi.service.UserInfoService;
import sejong.teemo.riotapi.api.external.LeagueExternalApi;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teemo.gg/api/v1")
public class LeagueApiV1 {

    private final UserInfoService userInfoService;
    private final LeagueExternalApi leagueExternalApi;

    @GetMapping("/league-to-summoner/{division}/{tier}/{queue}")
    public ResponseEntity<List<List<LeagueEntryDto>>> callApiLeagueToSummoner(@PathVariable("division") String division,
                                                                              @PathVariable("tier") String tier,
                                                                              @PathVariable("queue") String queue,
                                                                              @RequestParam("startPage") int startPage,
                                                                              @RequestParam("endPage") int endPage) {

        return ResponseEntity.ok(userInfoService.callApiLeague(division, tier, queue, startPage, endPage));
    }

    @GetMapping("/league/{division}/{tier}/{queue}")
    public ResponseEntity<List<LeagueEntryDto>> callApiLeague(@PathVariable("division") String division,
                                                              @PathVariable("tier") String tier,
                                                              @PathVariable("queue") String queue,
                                                              @RequestParam("page") int page) {

        return ResponseEntity.ok(userInfoService.callApiLeague(division, tier, queue, page));
    }

    @GetMapping("/league/{summonerId}")
    public ResponseEntity<LeagueEntryDto> callApiLeague(@PathVariable("summonerId") String summonerId) {
        return ResponseEntity.ok(leagueExternalApi.callRiotLeague(summonerId));
    }
}
