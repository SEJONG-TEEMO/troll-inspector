package sejong.teemo.riotapi.application.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sejong.teemo.riotapi.application.external.AccountExternalApi;
import sejong.teemo.riotapi.application.external.MatchExternalApi;
import sejong.teemo.riotapi.common.async.AsyncCall;
import sejong.teemo.riotapi.common.exception.ExceptionProvider;
import sejong.teemo.riotapi.common.exception.NotFoundException;
import sejong.teemo.riotapi.presentation.dto.Account;
import sejong.teemo.riotapi.presentation.dto.SummonerPerformance;
import sejong.teemo.riotapi.presentation.dto.match.MatchDataDto;
import sejong.teemo.riotapi.presentation.dto.match.MatchDto;
import sejong.teemo.riotapi.presentation.dto.match.ParticipantDto;
import sejong.teemo.riotapi.presentation.dto.match.TeamDto;

@Component
@RequiredArgsConstructor
@Slf4j
public class MatchService {

    private final MatchExternalApi matchExternalApi;
    private final AccountExternalApi accountExternalApi;

    public List<MatchDto> callRiotMatch(String gameName, String tagLine) {

        Account account = accountExternalApi.callRiotAccount(gameName, tagLine);

        log.info("account = {}", account);

        List<String> matchDtos = matchExternalApi.callRiotApiMatchPuuid(account.puuid());

        log.info("matchDtos = {}", matchDtos);
        AsyncCall<String, MatchDto> asyncCall = new AsyncCall<>(matchDtos);

        return asyncCall.execute(10, matchId ->
                MatchDto.of(gameName, tagLine, matchExternalApi.callRiotApiMatchMatchId(matchId))
        );
    }

    public List<SummonerPerformance> callRiotSummonerPerformance(String puuid) {
        List<String> matchDtos = matchExternalApi.callRiotApiMatchPuuid(puuid);

        AsyncCall<String, SummonerPerformance> asyncCall = new AsyncCall<>(matchDtos);

        return asyncCall.execute(10, matchId -> {
            MatchDataDto matchDataDto = matchExternalApi.callRiotApiMatchMatchId(matchId);

            ParticipantDto searchParticipant = matchDataDto.info().participants()
                    .stream()
                    .filter(participantDto -> Objects.equals(participantDto.puuid(), puuid))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException(ExceptionProvider.NOT_FOUND_SUMMONER));

            TeamDto teamDto = matchDataDto.info().teams().stream()
                    .filter(team -> Objects.equals(team.teamId(), searchParticipant.teamId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("teamId 가 다릅니다."));

            return this.getSummonerPerformance(searchParticipant, teamDto);
        });

    }

    @Deprecated
    public List<SummonerPerformance> callRiotSummonerPerformance(String gameName, String tagLine) {
        Account account = accountExternalApi.callRiotAccount(gameName, tagLine);

        List<String> matchDtos = matchExternalApi.callRiotApiMatchPuuid(account.puuid());

        AsyncCall<String, MatchDto> asyncCall = new AsyncCall<>(matchDtos);

        List<MatchDto> matchDtoList = asyncCall.execute(10, matchId ->
                MatchDto.of(gameName, tagLine, matchExternalApi.callRiotApiMatchMatchId(matchId))
        );

        return IntStream.rangeClosed(0, matchDtoList.size() - 1)
                .parallel()
                .mapToObj(index -> returnSummonerPerformanceTrackingTargetIdx(index, matchDtoList, account))
                .toList();
    }

    public List<MatchDataDto> callRiotMatch(String puuid) {

        List<String> matchDtos = matchExternalApi.callRiotApiMatchPuuid(puuid);

        AsyncCall<String, MatchDataDto> asyncCall = new AsyncCall<>(matchDtos);

        return asyncCall.execute(10, matchExternalApi::callRiotApiMatchMatchId);
    }

    @Deprecated
    private SummonerPerformance returnSummonerPerformanceTrackingTargetIdx(int index, List<MatchDto> matchDtoList,
                                                                           Account account) {
        List<ParticipantDto> participants = matchDtoList.get(index)
                .matchDataDto()
                .info()
                .participants();

        int targetIdx = IntStream.rangeClosed(0, participants.size() - 1)
                .filter(idx -> Objects.equals(participants.get(idx).puuid(), account.puuid()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(ExceptionProvider.NOT_FOUND_SUMMONER));

        return this.getSummonerPerformance(participants.get(targetIdx), null);
    }

    private SummonerPerformance getSummonerPerformance(ParticipantDto participantDto, TeamDto teamDto) {
        return SummonerPerformance.of(participantDto, teamDto);
    }
}
