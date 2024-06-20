package sejong.teemo.ingamesearch.ingame.api.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sejong.teemo.ingamesearch.ingame.facade.SummonerPerformanceFacade;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class InGameApiV1 {

    private final SummonerPerformanceFacade summonerPerformanceFacade;

    @GetMapping("/in-game/{gameName}/{tagLine}")
    public ResponseEntity<>
}
