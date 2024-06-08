package sejong.teemo.ingamesearch.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sejong.teemo.ingamesearch.async.AsyncCall;
import sejong.teemo.ingamesearch.dto.*;
import sejong.teemo.ingamesearch.service.SpectatorService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InGameFacade {

    private final SpectatorService spectatorService;

    public List<InGameView> inGame(String gameName, String tagLine) {

    }

    private List<InGameView> asyncRequestInGame(String gameName, String tagLine) {
        List<ChampionMastery> championMasteries = spectatorService.callApiSpectator(gameName, tagLine);

        AsyncCall<ChampionMastery, InGameView> asyncCall = new AsyncCall<>(championMasteries);

        asyncCall.execute(10, championMastery -> {
            Account account = spectatorService.callApiAccount(championMastery.puuid());
            SummonerDto summonerDto = spectatorService.callApiSummoner(championMastery.summonerId());
            LeagueEntryDto leagueEntryDto = spectatorService.callApiLeagueEntry(championMastery.summonerId());

            return InGameView.of(
                    championMastery.championId(),
                    account.gameName(),
                    account.tagLine(),
                    leagueEntryDto.tier(),
                    leagueEntryDto.rank(),
                    summonerDto.summonerLevel(),
                    summonerDto.profileIconId(),
                    championMastery.championLevel(),

                    )
        })
    }
}
