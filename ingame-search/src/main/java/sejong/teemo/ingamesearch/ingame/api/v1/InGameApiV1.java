package sejong.teemo.ingamesearch.ingame.api.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sejong.teemo.ingamesearch.ingame.dto.InGameView;
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
}
