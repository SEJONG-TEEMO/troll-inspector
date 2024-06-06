package sejong.teemo.riotapi.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sejong.teemo.riotapi.async.AsyncCall;
import sejong.teemo.riotapi.dto.Account;
import sejong.teemo.riotapi.service.AccountService;
import sejong.teemo.riotapi.service.MatchService;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MatchFacade {

    private final MatchService matchService;
    private final AccountService accountService;

    public List<String> callRiotMatch(String gameName, String tagLine) {

        Account account = accountService.callRiotAccount(gameName, tagLine);

        log.info("account = {}", account);

        List<String> matchDtos = matchService.callRiotApiMatchPuuid(account.puuid());

        log.info("matchDtos = {}", matchDtos);
        AsyncCall<String, String> asyncCall = new AsyncCall<>(matchDtos);

        return asyncCall.execute(10, matchService::callRiotApiMatchMatchId);
    }

    public List<String> callRiotMatch(String puuid) {

        List<String> matchDtos = matchService.callRiotApiMatchPuuid(puuid);

        AsyncCall<String, String> asyncCall = new AsyncCall<>(matchDtos);

        return asyncCall.execute(10, matchService::callRiotApiMatchMatchId);
    }
}
