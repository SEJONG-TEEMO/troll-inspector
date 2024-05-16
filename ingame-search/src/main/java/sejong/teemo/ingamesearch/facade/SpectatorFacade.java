package sejong.teemo.ingamesearch.facade;

import lombok.RequiredArgsConstructor;
import sejong.teemo.ingamesearch.dto.Account;
import sejong.teemo.ingamesearch.dto.Spectator;
import sejong.teemo.ingamesearch.facade.annotation.Facade;
import sejong.teemo.ingamesearch.service.SpectatorService;

@Facade
@RequiredArgsConstructor
public class SpectatorFacade {

    private final SpectatorService spectatorService;

    public Spectator callSpectator(String gameName, String tag) {

        Account account = spectatorService.callRiotPUUID(gameName, tag);
        return spectatorService.callRiotSpectatorV5(account.puuid());
    }
}
