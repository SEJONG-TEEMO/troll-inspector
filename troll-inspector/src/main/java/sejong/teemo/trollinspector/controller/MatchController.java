package sejong.teemo.trollinspector.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sejong.teemo.trollinspector.service.MatchService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/matches")
public class MatchController {

    private final MatchService matchService;

    @GetMapping("/ids/{gameName}/{tagLine}")
    public ResponseEntity<List<String>> getMatchIds(@PathVariable("gameName") String gameName,
                                                    @PathVariable("tagLine") String tagLine) throws Exception {
        List<String> matchIds = matchService.getMatchDetails(gameName, tagLine);
        return ResponseEntity.ok(matchIds);
    }
}

