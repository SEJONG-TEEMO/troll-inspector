package sejong.teemo.riotapi.presentation.api.v1;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sejong.teemo.riotapi.application.service.UserInfoService;
import sejong.teemo.riotapi.domain.userinfo.dto.LeagueEntryDto;
import sejong.teemo.riotapi.infrastructure.external.LeagueExternalApi;

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
