package sejong.teemo.ingamesearch.facade;

import lombok.RequiredArgsConstructor;
import sejong.teemo.ingamesearch.dto.Account;
import sejong.teemo.ingamesearch.dto.ChampionMastery;
import sejong.teemo.ingamesearch.dto.CurrentGameParticipant;
import sejong.teemo.ingamesearch.dto.Spectator;
import sejong.teemo.ingamesearch.facade.annotation.Facade;
import sejong.teemo.ingamesearch.service.SpectatorService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Facade
@RequiredArgsConstructor
public class SpectatorFacade {

    private final SpectatorService spectatorService;

    public List<ChampionMastery> callSpectator(String gameName, String tag) {

        Account account = spectatorService.callRiotPUUID(gameName, tag);
        Spectator spectator = spectatorService.callRiotSpectatorV5(account.puuid());

        return asyncCallChampionMastery(spectator.currentGameParticipants());
    }

    private List<ChampionMastery> asyncCallChampionMastery(List<CurrentGameParticipant> currentGameParticipants) {

        List<CompletableFuture<ChampionMastery>> futures = currentGameParticipants.stream()
                .map(currentGameParticipant -> CompletableFuture.supplyAsync(() ->
                                spectatorService.callRiotChampionMastery(currentGameParticipant.puuid(), currentGameParticipant.championId())
                        )).toList();

        return futures.stream().map(CompletableFuture::join).toList();
    }
}
