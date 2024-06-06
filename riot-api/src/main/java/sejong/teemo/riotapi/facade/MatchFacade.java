package sejong.teemo.riotapi.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sejong.teemo.riotapi.async.AsyncCall;
import sejong.teemo.riotapi.dto.Account;
import sejong.teemo.riotapi.dto.match.MatchDataDto;
import sejong.teemo.riotapi.dto.match.MatchDto;
import sejong.teemo.riotapi.service.AccountService;
import sejong.teemo.riotapi.service.MatchService;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MatchFacade {

    private final MatchService matchService;
    private final AccountService accountService;

    public List<MatchDto> callRiotMatch(String gameName, String tagLine) {

        Account account = accountService.callRiotAccount(gameName, tagLine);

        log.info("account = {}", account);

        List<String> matchDtos = matchService.callRiotApiMatchPuuid(account.puuid());

        log.info("matchDtos = {}", matchDtos);
        AsyncCall<String, MatchDto> asyncCall = new AsyncCall<>(matchDtos);

        return asyncCall.execute(10, matchId ->
                MatchDto.of(gameName, tagLine, matchService.callRiotApiMatchMatchId(matchId))
        );
    }

    public List<MatchDataDto> callRiotMatch(String puuid) {

        List<String> matchDtos = matchService.callRiotApiMatchPuuid(puuid);

        AsyncCall<String, MatchDataDto> asyncCall = new AsyncCall<>(matchDtos);

        return asyncCall.execute(10, matchService::callRiotApiMatchMatchId);
    }
}
