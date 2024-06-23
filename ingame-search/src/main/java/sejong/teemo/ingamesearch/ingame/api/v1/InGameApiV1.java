package sejong.teemo.ingamesearch.ingame.api.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sejong.teemo.ingamesearch.ingame.dto.InGameView;
import sejong.teemo.ingamesearch.ingame.dto.user.performance.UserPerformanceDto;
import sejong.teemo.ingamesearch.ingame.facade.InGameFacade;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class InGameApiV1 {

    private final InGameFacade inGameFacade;

    @GetMapping("/in-game")
    public ResponseEntity<List<InGameView>> inGameApi(@RequestParam("gameName") String gameName,
                                                      @RequestParam("tagLine") String tagLine) {

        return ResponseEntity.ok(inGameFacade.inGame(gameName, tagLine));
    }

    @GetMapping("/normal")
    public ResponseEntity<UserPerformanceDto> normalApi(@RequestParam("gameName") String gameName,
                                                        @RequestParam("tagLine") String tagLine) {

        return ResponseEntity.ok(inGameFacade.normal(gameName, tagLine));
    }

    @PatchMapping("/in-game")
    public ResponseEntity<Long> updateInGame(@RequestParam("gameName") String gameName,
                                             @RequestParam("tagLine") String tagLine) {

        return ResponseEntity.ok(inGameFacade.updateSummonerPerformance(gameName, tagLine));
    }
}
