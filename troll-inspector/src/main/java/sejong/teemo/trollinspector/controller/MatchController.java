package sejong.teemo.trollinspector.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sejong.teemo.trollinspector.domain.SummonerPerformance;
import sejong.teemo.trollinspector.service.MatchService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/matches")
@Slf4j
public class MatchController {

    private final MatchService matchService;

    @GetMapping("/ids/{gameName}/{tagLine}")
    public ResponseEntity<List<SummonerPerformance>> analyzeSummonerPerformance(@PathVariable("gameName") String gameName,
                                                                                @PathVariable("tagLine") String tagLine) throws Exception {
        List<SummonerPerformance> matchIds = matchService.analyzeSummonerPerformanceForPersonalKey(gameName, tagLine);

        return ResponseEntity.ok(matchIds);
    }
}

