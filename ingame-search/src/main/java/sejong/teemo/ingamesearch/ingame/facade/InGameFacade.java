package sejong.teemo.ingamesearch.ingame.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sejong.teemo.ingamesearch.champion.service.ChampionService;
import sejong.teemo.ingamesearch.common.async.AsyncCall;
import sejong.teemo.ingamesearch.ingame.dto.InGameView;
import sejong.teemo.ingamesearch.ingame.dto.SpectatorDto;
import sejong.teemo.ingamesearch.ingame.dto.champion.Champion;
import sejong.teemo.ingamesearch.ingame.dto.champion.ChampionMastery;
import sejong.teemo.ingamesearch.ingame.dto.summoner.SummonerPerformance;
import sejong.teemo.ingamesearch.ingame.dto.user.Account;
import sejong.teemo.ingamesearch.ingame.dto.user.LeagueEntryDto;
import sejong.teemo.ingamesearch.ingame.dto.user.SummonerDto;
import sejong.teemo.ingamesearch.ingame.dto.user.UserInfoDto;
import sejong.teemo.ingamesearch.ingame.repository.InGameRepository;
import sejong.teemo.ingamesearch.ingame.service.InGameService;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class InGameFacade {

    private final InGameService inGameService;
    private final ChampionService championService;
    private final InGameRepository inGameRepository;

    public List<InGameView> inGame(String gameName, String tagLine) {

        String checkPuuid = getPuuid(gameName, tagLine);

        SpectatorDto spectatorDto = inGameService.callApiSpectator(checkPuuid);

        AsyncCall<ChampionMastery, InGameView> asyncCall = new AsyncCall<>(spectatorDto.championMasteryList());

        return asyncCall.execute(10, championMastery -> {
            String puuid = championMastery.puuid();

            byte[] championImage = championService.fetchChampionImage(championMastery.championId());

            UserInfoDto userInfo = getUserInfo(puuid);

            Champion champion = getInGameView(puuid, championMastery.championId());

            return InGameView.of(
                    championImage,
                    userInfo.gameName(),
                    userInfo.tagLine(),
                    userInfo.tier(),
                    userInfo.rank(),
                    userInfo.summonerLevel(),
                    userInfo.profileIconId(),
                    championMastery.championLevel(),
                    championMastery.championPoints(),
                    champion
            );
        });
    }

    private String getPuuid(String gameName, String tagLine) {
        return inGameRepository.findByGameNameAndTagLine(gameName, tagLine)
                .orElseGet(() -> inGameService.callApiAccount(gameName, tagLine).puuid());
    }

    private UserInfoDto getUserInfo(String puuid) {
        Account account = inGameService.callApiAccount(puuid);
        SummonerDto summonerDto = inGameService.callApiSummoner(account.puuid());
        LeagueEntryDto leagueEntryDto = inGameService.callApiLeagueEntry(summonerDto.summonerId());

        return UserInfoDto.of(leagueEntryDto, summonerDto, account);
    }

    private Champion getInGameView(String puuid, Long championId) {

        List<SummonerPerformance> performances = inGameService
                .callRiotSummonerPerformance(puuid)
                .stream()
                .filter(summonerPerformance -> Objects.equals(summonerPerformance.championId(), championId.intValue()))
                .toList();


        int killSum = performances.stream().mapToInt(SummonerPerformance::kills).sum();
        int deathSum = performances.stream().mapToInt(SummonerPerformance::deaths).sum();
        int assistSum = performances.stream().mapToInt(SummonerPerformance::assists).sum();
        int recentGameCount = performances.size();
        double kda = performances.stream().mapToDouble(SummonerPerformance::kda).sum();
        int wins = (int) performances.stream().filter(SummonerPerformance::wins).count();
        int losses = (int) performances.stream().filter(summonerPerformance -> !summonerPerformance.wins()).count();

        return Champion.of(killSum, deathSum, assistSum, recentGameCount, kda / recentGameCount, wins, losses);
    }
}
