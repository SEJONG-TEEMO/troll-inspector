package sejong.teemo.riotapi.api.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sejong.teemo.riotapi.dto.SpectatorDto;
import sejong.teemo.riotapi.facade.SpectatorFacade;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teemo.gg/api/v1")
public class SpectatorApiV1 {

    private final SpectatorFacade spectatorFacade;

    @GetMapping("/spectator/{puuid}")
    public ResponseEntity<SpectatorDto> callApiSpectator(@PathVariable String puuid) {
        return ResponseEntity.ok(spectatorFacade.callSpectator(puuid));
    }
}
