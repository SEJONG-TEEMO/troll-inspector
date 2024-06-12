package sejong.teemo.trollinspector.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import sejong.teemo.trollinspector.service.MatchService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/troll-inspector/v1")
@Slf4j
public class MatchController {

    private final MatchService matchService;

    @GetMapping("/ids/{gameName}/{tagLine}")
    public Mono<ResponseEntity<Void>> analyzeSummonerPerformance(@PathVariable("gameName") String gameName,
                                                                 @PathVariable("tagLine") String tagLine) {
        return matchService.analyzeSummonerPerformance(gameName, tagLine)
                .then(Mono.just(ResponseEntity.ok().build()));
    }

    @GetMapping("/ids/{puuid}")
    public Mono<ResponseEntity<Void>> analyzeSummonerPerformance(@PathVariable("puuid") String puuid) {
        return matchService.analyzeSummonerPerformance(puuid)
                .then(Mono.just(ResponseEntity.ok().build()));
    }
}

