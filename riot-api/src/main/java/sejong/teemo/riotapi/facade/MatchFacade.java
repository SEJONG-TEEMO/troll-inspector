package sejong.teemo.riotapi.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sejong.teemo.riotapi.async.AsyncCall;
import sejong.teemo.riotapi.dto.Account;
import sejong.teemo.riotapi.dto.SummonerPerformance;
import sejong.teemo.riotapi.dto.match.InfoDto;
import sejong.teemo.riotapi.dto.match.MatchDataDto;
import sejong.teemo.riotapi.dto.match.MatchDto;
import sejong.teemo.riotapi.dto.match.ParticipantDto;
import sejong.teemo.riotapi.exception.ExceptionProvider;
import sejong.teemo.riotapi.exception.FailedApiCallingException;
import sejong.teemo.riotapi.service.AccountService;
import sejong.teemo.riotapi.service.MatchService;

import java.util.List;
import java.util.stream.IntStream;

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

    public List<SummonerPerformance> callRiotSummonerPerformance(String gameName, String tagLine) {
        Account account = accountService.callRiotAccount(gameName, tagLine);

        List<String> matchDtos = matchService.callRiotApiMatchPuuid(account.puuid());

        AsyncCall<String, MatchDto> asyncCall = new AsyncCall<>(matchDtos);

        List<MatchDto> matchDtoList = asyncCall.execute(10, matchId ->
                MatchDto.of(gameName, tagLine, matchService.callRiotApiMatchMatchId(matchId))
        );

        return matchDtoList.stream()
                .map(MatchDto::matchDataDto)
                .map(MatchDataDto::info)
                .map(InfoDto::participants)
                .map(this::mapToSummonerPerformance)
                .findAny()
                .orElseThrow(() -> new FailedApiCallingException(ExceptionProvider.RIOT_ACCOUNT_API_CALL_FAILED));
    }

    public List<MatchDataDto> callRiotMatch(String puuid) {

        List<String> matchDtos = matchService.callRiotApiMatchPuuid(puuid);

        AsyncCall<String, MatchDataDto> asyncCall = new AsyncCall<>(matchDtos);

        return asyncCall.execute(10, matchService::callRiotApiMatchMatchId);
    }

    private List<SummonerPerformance> mapToSummonerPerformance(List<ParticipantDto> participants) {
        return IntStream.rangeClosed(0, participants.size() - 1)
                .parallel()
                .mapToObj(targetIdx -> getSummonerPerformance(participants, targetIdx))
                .toList();
    }

    private SummonerPerformance getSummonerPerformance(List<ParticipantDto> participants, int targetIdx) {
        ParticipantDto participantDto = participants.get(targetIdx);
        Account account = accountService.callRiotAccount(participantDto.puuid());

        return SummonerPerformance.of(targetIdx, participantDto, account);
    }
}
