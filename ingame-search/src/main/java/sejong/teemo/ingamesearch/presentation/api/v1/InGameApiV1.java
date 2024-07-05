package sejong.teemo.ingamesearch.presentation.api.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sejong.teemo.ingamesearch.domain.dto.InGameView;
import sejong.teemo.ingamesearch.domain.dto.user.performance.UserPerformanceDto;
import sejong.teemo.ingamesearch.application.service.InGameService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class InGameApiV1 {

    private final InGameService inGameService;

    @GetMapping("/in-game")
    public ResponseEntity<List<InGameView>> inGameApi(@RequestParam("gameName") String gameName,
                                                      @RequestParam("tagLine") String tagLine) {

        return ResponseEntity.ok(inGameService.inGame(gameName, tagLine));
    }

    @GetMapping("/performance")
    public ResponseEntity<UserPerformanceDto> performanceApi(@RequestParam("gameName") String gameName,
                                                             @RequestParam("tagLine") String tagLine) {

        return ResponseEntity.ok(inGameService.viewUserGamePerformance(gameName, tagLine));
    }

    @PatchMapping("/performance")
    public ResponseEntity<UserPerformanceDto> updateInPerformance(@RequestParam("gameName") String gameName,
                                                                  @RequestParam("tagLine") String tagLine) {

        return ResponseEntity.ok(inGameService.updateSummonerPerformance(gameName, tagLine));
    }
}
