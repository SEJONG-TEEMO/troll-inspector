package sejong.teemo.riotapi.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sejong.teemo.riotapi.async.AsyncCall;
import sejong.teemo.riotapi.dto.*;
import sejong.teemo.riotapi.facade.annotation.Facade;
import sejong.teemo.riotapi.service.SpectatorService;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class SpectatorFacade {

    private final SpectatorService spectatorService;

    public SpectatorDto callSpectator(String gameName, String tag) {

        Account account = spectatorService.callRiotPUUID(gameName, tag);
        log.info("account = {}", account);

        Spectator spectator = spectatorService.callRiotSpectatorV5(account.puuid());
        log.info("spectator = {}", spectator);

        return asyncCallChampionMastery(spectator);
    }

    private SpectatorDto asyncCallChampionMastery(Spectator spectator) {

        AsyncCall<CurrentGameParticipant, ChampionMastery> asyncCall = new AsyncCall<>(spectator.participants());

        List<ChampionMastery> championMasteries = asyncCall.execute(10, participant ->
                spectatorService.callRiotChampionMastery(participant.puuid(), participant.championId())
        );

        return SpectatorDto.of(spectator.gameId(), spectator.gameType(), championMasteries);
    }
}
