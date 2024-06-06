package sejong.teemo.riotapi.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sejong.teemo.riotapi.async.AsyncCall;
import sejong.teemo.riotapi.dto.Account;
import sejong.teemo.riotapi.dto.MatchDto;
import sejong.teemo.riotapi.dto.SummonerPerformanceRecord;
import sejong.teemo.riotapi.service.AccountService;
import sejong.teemo.riotapi.service.MatchService;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MatchFacade {

    private final MatchService matchService;
    private final AccountService accountService;

    public List<SummonerPerformanceRecord> callRiotMatch(String gameName, String tagLine) {

        Account account = accountService.callRiotAccount(gameName, tagLine);

        List<MatchDto> matchDtos = matchService.callRiotApiMatchPuuid(account.puuid());

        AsyncCall<MatchDto, SummonerPerformanceRecord> asyncCall = new AsyncCall<>(matchDtos);

        return asyncCall.execute(10, matchDto -> matchService.callRiotApiMatchMatchId(matchDto.matchId()));
    }

    public List<SummonerPerformanceRecord> callRiotMatch(String puuid) {

        List<MatchDto> matchDtos = matchService.callRiotApiMatchPuuid(puuid);

        AsyncCall<MatchDto, SummonerPerformanceRecord> asyncCall = new AsyncCall<>(matchDtos);

        return asyncCall.execute(10, matchDto -> matchService.callRiotApiMatchMatchId(matchDto.matchId()));
    }
}
