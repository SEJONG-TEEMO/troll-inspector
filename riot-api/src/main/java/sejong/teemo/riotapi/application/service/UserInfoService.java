package sejong.teemo.riotapi.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sejong.teemo.riotapi.application.external.AccountExternalApi;
import sejong.teemo.riotapi.application.external.LeagueExternalApi;
import sejong.teemo.riotapi.application.external.SummonerExternalApi;
import sejong.teemo.riotapi.common.async.AsyncCall;
import sejong.teemo.riotapi.common.async.AsyncCallRiotApi;
import sejong.teemo.riotapi.common.dto.Account;
import sejong.teemo.riotapi.common.dto.LeagueEntryDto;
import sejong.teemo.riotapi.common.dto.SummonerDto;
import sejong.teemo.riotapi.common.dto.UserInfoDto;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserInfoService {

    private final LeagueExternalApi leagueExternalApi;
    private final SummonerExternalApi summonerExternalApi;
    private final AccountExternalApi accountExternalApi;

    public List<UserInfoDto> callApiUserInfo(String division, String tier, String queue, int page) {
        List<LeagueEntryDto> leagueEntryDtos = leagueExternalApi.callRiotLeague(division, tier, queue, page);

        AsyncCall<LeagueEntryDto, UserInfoDto> asyncCall = new AsyncCall<>(leagueEntryDtos);

        return asyncCall.execute(10, leagueEntryDto -> {
            SummonerDto summonerDto = summonerExternalApi.callApiSummoner(leagueEntryDto.summonerId());
            Account account = accountExternalApi.callRiotAccount(summonerDto.puuid());

            return UserInfoDto.of(leagueEntryDto, summonerDto, account);
        });
    }

    public List<List<LeagueEntryDto>> callApiLeague(String division, String tier, String queue, int startPage,
                                                    int endPage) {
        AsyncCallRiotApi<LeagueEntryDto> callRiotApi = new AsyncCallRiotApi<>(startPage, endPage);

        return callRiotApi.execute(10, page -> leagueExternalApi.callRiotLeague(division, tier, queue, page));
    }

    public List<LeagueEntryDto> callApiLeague(String division, String tier, String queue, int page) {
        return leagueExternalApi.callRiotLeague(division, tier, queue, page);
    }

    public SummonerDto callApiSummoner(String encryptedSummonerId) {
        return summonerExternalApi.callApiSummoner(encryptedSummonerId);
    }

    public SummonerDto callApiSummonerByPuuid(String puuid) {
        return summonerExternalApi.callApiSummonerByPuuid(puuid);
    }

    public Account callApiAccount(String puuid) {
        return accountExternalApi.callRiotAccount(puuid);
    }
}
