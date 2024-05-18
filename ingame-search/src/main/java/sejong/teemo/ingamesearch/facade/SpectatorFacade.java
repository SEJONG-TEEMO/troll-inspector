package sejong.teemo.ingamesearch.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sejong.teemo.ingamesearch.dto.Account;
import sejong.teemo.ingamesearch.dto.ChampionMastery;
import sejong.teemo.ingamesearch.dto.CurrentGameParticipant;
import sejong.teemo.ingamesearch.dto.Spectator;
import sejong.teemo.ingamesearch.exception.ExceptionProvider;
import sejong.teemo.ingamesearch.exception.FailedApiCallingException;
import sejong.teemo.ingamesearch.facade.annotation.Facade;
import sejong.teemo.ingamesearch.service.SpectatorService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Facade
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

    private List<ChampionMastery> asyncCallChampionMastery(List<CurrentGameParticipant> currentGameParticipants) {

        try (ExecutorService executorService = Executors.newFixedThreadPool(10)) {
            List<CompletableFuture<ChampionMastery>> futures = currentGameParticipants.stream()
                    .map(currentGameParticipant -> CompletableFuture.supplyAsync(() ->
                                    spectatorService.callRiotChampionMastery(currentGameParticipant.puuid(), currentGameParticipant.championId())
                            , executorService)).toList();

            return futures.stream().map(CompletableFuture::join).toList();
        } catch (Exception e) {
            log.error("champion mastery call error = {}", e.getMessage());
            throw new FailedApiCallingException(ExceptionProvider.RIOT_CHAMPION_MASTERY_API_CALL_FAILED);
        }
    }
}
