package sejong.teemo.riotapi.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sejong.teemo.riotapi.async.AsyncCall;
import sejong.teemo.riotapi.dto.Account;
import sejong.teemo.riotapi.dto.ChampionMastery;
import sejong.teemo.riotapi.dto.CurrentGameParticipant;
import sejong.teemo.riotapi.dto.Spectator;
import sejong.teemo.riotapi.facade.annotation.Facade;
import sejong.teemo.riotapi.service.SpectatorService;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class SpectatorFacade {

    private final SpectatorService spectatorService;

    public List<ChampionMastery> callSpectator(String gameName, String tag) {

        Account account = spectatorService.callRiotPUUID(gameName, tag);
        log.info("account = {}", account);

        Spectator spectator = spectatorService.callRiotSpectatorV5(account.puuid());
        log.info("spectator = {}", spectator);

        return asyncCallChampionMastery(spectator.participants());
    }

    private List<ChampionMastery> asyncCallChampionMastery(List<CurrentGameParticipant> participants) {

        AsyncCall<CurrentGameParticipant, ChampionMastery> asyncCall = new AsyncCall<>(participants);

        return asyncCall.execute(10, participant ->
                spectatorService.callRiotChampionMastery(participant.puuid(), participant.championId())
        );
    }
}
