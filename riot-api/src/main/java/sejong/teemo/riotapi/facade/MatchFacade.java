package sejong.teemo.riotapi.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sejong.teemo.riotapi.async.AsyncCall;
import sejong.teemo.riotapi.dto.Account;
import sejong.teemo.riotapi.dto.SummonerPerformance;
import sejong.teemo.riotapi.dto.match.MatchDataDto;
import sejong.teemo.riotapi.dto.match.MatchDto;
import sejong.teemo.riotapi.dto.match.ParticipantDto;
import sejong.teemo.riotapi.exception.ExceptionProvider;
import sejong.teemo.riotapi.exception.NotFoundException;
import sejong.teemo.riotapi.service.AccountService;
import sejong.teemo.riotapi.service.MatchService;

import java.util.List;
import java.util.Objects;
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

    public List<SummonerPerformance> callRiotSummonerPerformance(String puuid) {
        List<String> matchDtos = matchService.callRiotApiMatchPuuid(puuid);

        AsyncCall<String, SummonerPerformance> asyncCall = new AsyncCall<>(matchDtos);

        return asyncCall.execute(10, matchId -> {
            MatchDataDto matchDataDto = matchService.callRiotApiMatchMatchId(matchId);

            ParticipantDto searchParticipant = matchDataDto.info().participants().stream()
                    .filter(participantDto -> Objects.equals(participantDto.puuid(), puuid))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException(ExceptionProvider.NOT_FOUND_SUMMONER));

            return this.getSummonerPerformance(searchParticipant);
        });

    }

    @Deprecated
    public List<SummonerPerformance> callRiotSummonerPerformance(String gameName, String tagLine) {
        Account account = accountService.callRiotAccount(gameName, tagLine);

        List<String> matchDtos = matchService.callRiotApiMatchPuuid(account.puuid());

        AsyncCall<String, MatchDto> asyncCall = new AsyncCall<>(matchDtos);

        List<MatchDto> matchDtoList = asyncCall.execute(10, matchId ->
                MatchDto.of(gameName, tagLine, matchService.callRiotApiMatchMatchId(matchId))
        );

        return IntStream.rangeClosed(0, matchDtoList.size() - 1)
                .parallel()
                .mapToObj(index-> returnSummonerPerformanceTrackingTargetIdx(index, matchDtoList, account))
                .toList();
    }

    public List<MatchDataDto> callRiotMatch(String puuid) {

        List<String> matchDtos = matchService.callRiotApiMatchPuuid(puuid);

        AsyncCall<String, MatchDataDto> asyncCall = new AsyncCall<>(matchDtos);

        return asyncCall.execute(10, matchService::callRiotApiMatchMatchId);
    }

    @Deprecated
    private SummonerPerformance returnSummonerPerformanceTrackingTargetIdx(int index, List<MatchDto> matchDtoList, Account account) {
        List<ParticipantDto> participants = matchDtoList.get(index)
                .matchDataDto()
                .info()
                .participants();

        int targetIdx = IntStream.rangeClosed(0, participants.size() - 1)
                .filter(idx -> Objects.equals(participants.get(idx).puuid(), account.puuid()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(ExceptionProvider.NOT_FOUND_SUMMONER));

        return this.getSummonerPerformance(participants.get(targetIdx));
    }

    private SummonerPerformance getSummonerPerformance(ParticipantDto participantDto) {
        return SummonerPerformance.from(participantDto);
    }
}
