package sejong.teemo.ingamesearch.ingame.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sejong.teemo.ingamesearch.ingame.async.AsyncCall;
import sejong.teemo.ingamesearch.dto.*;
import sejong.teemo.ingamesearch.ingame.dto.ChampionMastery;
import sejong.teemo.ingamesearch.ingame.dto.InGameView;
import sejong.teemo.ingamesearch.ingame.service.SpectatorService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InGameFacade {

    private final SpectatorService spectatorService;

    public List<InGameView> inGame(String gameName, String tagLine) {
        return null;
    }

    private List<InGameView> asyncRequestInGame(String gameName, String tagLine) {
        List<ChampionMastery> championMasteries = spectatorService.callApiSpectator(gameName, tagLine);

        AsyncCall<ChampionMastery, InGameView> asyncCall = new AsyncCall<>(championMasteries);

        return null;
    }
}
