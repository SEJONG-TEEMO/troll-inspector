package sejong.teemo.riotapi.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sejong.teemo.riotapi.common.async.AsyncCall;
import sejong.teemo.riotapi.domain.spectator.dto.ChampionMastery;
import sejong.teemo.riotapi.domain.spectator.dto.CurrentGameParticipant;
import sejong.teemo.riotapi.domain.spectator.dto.Spectator;
import sejong.teemo.riotapi.domain.spectator.dto.SpectatorDto;
import sejong.teemo.riotapi.infrastructure.external.SpectatorExternalApi;

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
