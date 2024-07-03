package sejong.teemo.ingamesearch.ingame.facade;

import com.github.benmanes.caffeine.cache.Cache;
import io.github.bucket4j.Bucket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sejong.teemo.ingamesearch.champion.service.ChampionService;
import sejong.teemo.ingamesearch.common.async.AsyncCall;
import sejong.teemo.ingamesearch.ingame.dto.InGameView;
import sejong.teemo.ingamesearch.ingame.dto.SpectatorDto;
import sejong.teemo.ingamesearch.ingame.dto.champion.Champion;
import sejong.teemo.ingamesearch.ingame.dto.champion.ChampionMastery;
import sejong.teemo.ingamesearch.ingame.dto.normal.NormalView;
import sejong.teemo.ingamesearch.ingame.dto.summoner.SummonerPerformance;
import sejong.teemo.ingamesearch.ingame.dto.user.*;
import sejong.teemo.ingamesearch.ingame.dto.user.performance.UserChampionPerformanceDto;
import sejong.teemo.ingamesearch.ingame.dto.user.performance.UserPerformanceDto;
import sejong.teemo.ingamesearch.ingame.entity.SummonerPerformanceInfo;
import sejong.teemo.ingamesearch.ingame.entity.UserInfo;
import sejong.teemo.ingamesearch.ingame.repository.InGameRepository;
import sejong.teemo.ingamesearch.ingame.repository.QueryDslRepository;
import sejong.teemo.ingamesearch.ingame.repository.SummonerPerformanceInfoRepository;
import sejong.teemo.ingamesearch.ingame.service.InGameService;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class InGameFacade {

    private final InGameService inGameService;
    private final ChampionService championService;
    private final InGameRepository inGameRepository;
    private final SummonerPerformanceInfoRepository summonerPerformanceInfoRepository;
    private final QueryDslRepository queryDslRepository;
    private final Map<Integer, String> mapToChampions;
    private final Cache<String, Boolean> cacheGameNameAndTagLine;

    public List<InGameView> inGame(String gameName, String tagLine) {

        String checkPuuid = getPuuid(gameName, tagLine);

        SpectatorDto spectatorDto = inGameService.callApiSpectator(checkPuuid);

        AsyncCall<ChampionMastery, InGameView> asyncCall = new AsyncCall<>(spectatorDto.championMasteryList());

        return asyncCall.execute(10, championMastery -> {
            String puuid = championMastery.puuid();

            byte[] championImage = championService.fetchChampionImage(championMastery.championId());

            UserInfoDto userInfo = getUserInfoDto(puuid);

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

    @Transactional
    public UserPerformanceDto updateSummonerPerformance(String gameName, String tagLine) {

        if(cacheGameNameAndTagLine.getIfPresent(gameName + "#" + tagLine) != null) {
            throw new IllegalArgumentException("업데이트를 진행할 수 없습니다. (2분 뒤에 다시 진행해주세요.)");
        }
        
        cacheGameNameAndTagLine.put(gameName + "#" + tagLine, true);

        UserInfo userInfo = getUserInfo(gameName, tagLine);

        List<SummonerPerformance> summonerPerformances = inGameService.callRiotSummonerPerformance(userInfo.getPuuid());

        List<SummonerPerformanceInfo> performanceInfos = summonerPerformanceInfoRepository.findByUserInfoId(userInfo.getId());

        update(summonerPerformances, userInfo, performanceInfos);

        List<NormalView> normalViews = queryDslRepository.findRecentGamesByUserId(userInfo.getId());

        return UserPerformanceDto.of(getUserProfileDto(normalViews), getUserChampionPerformanceDtos(normalViews));
    }

    @Transactional
    public UserPerformanceDto viewUserGamePerformance(String gameName, String tagLine) {
        UserInfo userInfo = getUserInfo(gameName, tagLine);

        if(!summonerPerformanceInfoRepository.existsByUserInfoId(userInfo.getId())) {

            List<SummonerPerformanceInfo> summonerPerformanceInfos = inGameService.callRiotSummonerPerformance(userInfo.getPuuid())
                    .stream()
                    .map(summonerPerformance -> SummonerPerformanceInfo.of(summonerPerformance, userInfo))
                    .toList();

            summonerPerformanceInfoRepository.saveAll(summonerPerformanceInfos);
        }

        List<NormalView> normalViews = queryDslRepository.findRecentGamesByUserId(userInfo.getId());

        return UserPerformanceDto.of(getUserProfileDto(normalViews), getUserChampionPerformanceDtos(normalViews));
    }

    private void update(List<SummonerPerformance> summonerPerformances, UserInfo userInfo, List<SummonerPerformanceInfo> performanceInfos) {
        IntStream.rangeClosed(0, summonerPerformances.size() - 1)
                .forEach(idx -> queryDslRepository.updateSummonerPerformanceInfo(
                        summonerPerformances.get(idx),
                        userInfo.getId(),
                        performanceInfos.get(idx).getId())
                );
    }

    private UserProfileDto getUserProfileDto(List<NormalView> normalViews) {
        return UserProfileDto.from(normalViews.getFirst());
    }

    private List<UserChampionPerformanceDto> getUserChampionPerformanceDtos(List<NormalView> normalViews) {
        return normalViews
                .stream()
                .map(normalView -> UserChampionPerformanceDto.of(mapToChampions.get(normalView.championId()), normalView))
                .toList();
    }

    private String getPuuid(String gameName, String tagLine) {
        return inGameRepository.findPuuidByGameNameAndTagLine(gameName, tagLine)
                .orElseGet(() -> inGameService.callApiAccount(gameName, tagLine).puuid());
    }

    private UserInfo getUserInfo(String gameName, String tagLine) {
        return inGameRepository.findByGameNameAndTagLine(gameName, tagLine)
                .orElseGet(() -> {
                    UserInfoDto userInfoDto = getUserInfoDto(gameName, tagLine);

                    return inGameRepository.save(UserInfo.from(userInfoDto));
                });
    }

    private UserInfoDto getUserInfoDto(String puuid) {
        Account account = inGameService.callApiAccount(puuid);
        SummonerDto summonerDto = inGameService.callApiSummoner(account.puuid());
        LeagueEntryDto leagueEntryDto = inGameService.callApiLeagueEntry(summonerDto.id());

        return UserInfoDto.of(leagueEntryDto, summonerDto, account);
    }

    private UserInfoDto getUserInfoDto(String gameName, String tagLine) {
        Account account = inGameService.callApiAccount(gameName, tagLine);
        log.info("account {}", account.puuid());
        SummonerDto summonerDto = inGameService.callApiSummoner(account.puuid());
        log.info("summoner {}", summonerDto.id());
        LeagueEntryDto leagueEntryDto = inGameService.callApiLeagueEntry(summonerDto.id());
        log.info("league {}", leagueEntryDto);

        return UserInfoDto.of(leagueEntryDto, summonerDto, account);
    }

    @Deprecated
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
        int wins = (int) performances.stream().filter(SummonerPerformance::win).count();
        int losses = (int) performances.stream().filter(summonerPerformance -> !summonerPerformance.win()).count();

        return Champion.of(killSum, deathSum, assistSum, recentGameCount, kda / recentGameCount, wins, losses);
    }
}
