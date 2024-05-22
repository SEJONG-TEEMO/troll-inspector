package sejong.teemo.trollinspector.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sejong.teemo.trollinspector.record.GameInspectorRecord;
import sejong.teemo.trollinspector.record.SummonerPerformanceRecord;
import sejong.teemo.trollinspector.service.PlayerStatsService;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/matches/search")
@Slf4j
public class MatchSearchController {

    private final PlayerStatsService playerStatsService;

    @GetMapping("/ids")
    public ResponseEntity<List<SummonerPerformanceRecord>> analyzeSummonerPerformance(@RequestParam String gameName) {
        List<SummonerPerformanceRecord> searchResponse = playerStatsService.searchGameData(gameName);
        log.info(searchResponse.toString());
        return ResponseEntity.ok(searchResponse);
    }

    @GetMapping("/ids1")
    public ResponseEntity<GameInspectorRecord> analyzeSummonerPerformance2(@RequestParam String gameName) throws IOException {
        GameInspectorRecord stringSearchResponse = playerStatsService.performAnalysis(gameName);
        log.info(stringSearchResponse.toString());
        return ResponseEntity.ok(stringSearchResponse);
    }
}
