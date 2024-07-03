package sejong.teemo.riotapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sejong.teemo.riotapi.common.async.AsyncCall;
import sejong.teemo.riotapi.dto.*;
import sejong.teemo.riotapi.api.external.SpectatorExternalApi;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class SpectatorService {

    private final SpectatorExternalApi spectatorExternalApi;

    public SpectatorDto callSpectator(String puuid) {

        Spectator spectator = spectatorExternalApi.callRiotSpectatorV5(puuid);
        log.info("spectator = {}", spectator);

        return asyncCallChampionMastery(spectator);
    }

    private SpectatorDto asyncCallChampionMastery(Spectator spectator) {

        AsyncCall<CurrentGameParticipant, ChampionMastery> asyncCall = new AsyncCall<>(spectator.participants());

        List<ChampionMastery> championMasteries = asyncCall.execute(10, participant ->
                spectatorExternalApi.callRiotChampionMastery(participant.puuid(), participant.championId())
        );

        return SpectatorDto.of(spectator.gameId(), spectator.gameType(), championMasteries);
    }
}
