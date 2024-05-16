package sejong.teemo.ingamesearch.api.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sejong.teemo.ingamesearch.facade.SpectatorFacade;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SpectatorApiV1 {

    private final SpectatorFacade spectatorFacade;
}