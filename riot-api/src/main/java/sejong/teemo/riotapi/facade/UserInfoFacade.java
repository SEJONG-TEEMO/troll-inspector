package sejong.teemo.riotapi.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sejong.teemo.riotapi.async.AsyncCall;
import sejong.teemo.riotapi.async.AsyncCallRiotApi;
import sejong.teemo.riotapi.dto.Account;
import sejong.teemo.riotapi.dto.LeagueEntryDto;
import sejong.teemo.riotapi.dto.SummonerDto;
import sejong.teemo.riotapi.dto.UserInfoDto;
import sejong.teemo.riotapi.service.AccountService;
import sejong.teemo.riotapi.service.LeagueService;
import sejong.teemo.riotapi.service.SummonerService;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserInfoFacade {
    
    private final LeagueService leagueService;
    private final SummonerService summonerService;
    private final AccountService accountService;

    public List<UserInfoDto> callApiUserInfo(String division, String tier, String queue, int page) {
        List<LeagueEntryDto> leagueEntryDtos = leagueService.callRiotLeague(division, tier, queue, page);

        AsyncCall<LeagueEntryDto, UserInfoDto> asyncCall = new AsyncCall<>(leagueEntryDtos);

        return asyncCall.execute(10, leagueEntryDto -> {
            SummonerDto summonerDto = summonerService.callApiSummoner(leagueEntryDto.summonerId());
            Account account = accountService.callRiotAccount(summonerDto.puuid());

            return UserInfoDto.of(leagueEntryDto, summonerDto, account);
        });
    }

    public List<List<LeagueEntryDto>> callApiLeague(String division, String tier, String queue, int startPage, int endPage) {
        AsyncCallRiotApi<LeagueEntryDto> callRiotApi = new AsyncCallRiotApi<>(startPage, endPage);

        return callRiotApi.execute(10, page -> leagueService.callRiotLeague(division, tier, queue, page));
    }

    public List<LeagueEntryDto> callApiLeague(String division, String tier, String queue, int page) {
        return leagueService.callRiotLeague(division, tier, queue, page);
    }

    public SummonerDto callApiSummoner(String encryptedSummonerId) {
        return summonerService.callApiSummoner(encryptedSummonerId);
    }

    public SummonerDto callApiSummonerByPuuid(String puuid) {
        return summonerService.callApiSummonerByPuuid(puuid);
    }

    public Account callApiAccount(String puuid) {
        return accountService.callRiotAccount(puuid);
    }
}
