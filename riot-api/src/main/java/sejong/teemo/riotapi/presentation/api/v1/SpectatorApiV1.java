package sejong.teemo.riotapi.presentation.api.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sejong.teemo.riotapi.presentation.dto.SpectatorDto;
import sejong.teemo.riotapi.application.service.SpectatorService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teemo.gg/api/v1")
public class SpectatorApiV1 {

    private final SpectatorService spectatorService;

    @GetMapping("/spectator/{puuid}")
    public ResponseEntity<SpectatorDto> callApiSpectator(@PathVariable("puuid") String puuid) {
        return ResponseEntity.ok(spectatorService.callSpectator(puuid));
    }
}
