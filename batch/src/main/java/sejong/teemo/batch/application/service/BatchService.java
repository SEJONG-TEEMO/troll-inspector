package sejong.teemo.batch.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import sejong.teemo.batch.common.async.AsyncCall;
import sejong.teemo.batch.domain.dto.Account;
import sejong.teemo.batch.domain.dto.LeagueEntryDto;
import sejong.teemo.batch.domain.dto.SummonerDto;
import sejong.teemo.batch.domain.dto.UserInfoDto;
import sejong.teemo.batch.common.exception.FailedApiCallingException;
import sejong.teemo.batch.common.generator.UriGenerator;
import sejong.teemo.batch.common.property.RiotApiProperties;

import java.util.List;

import static org.springframework.http.MediaType.*;
import static sejong.teemo.batch.common.exception.ExceptionProvider.*;
import static sejong.teemo.batch.common.generator.UriGenerator.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class BatchService {

    private final RestClient restClient;
    private final RiotApiProperties riotApiProperties;

    private static final String API_KEY = "X-Riot-Token";

    @Retryable(
            retryFor = {FailedApiCallingException.class, RuntimeException.class},
            backoff = @Backoff(delay = 60000, multiplier = 2),
            recover = "recover"
    )
    public List<UserInfoDto> callApiUserInfo(String division, String tier, String queue, int page) {

        log.info("callApiUserInfo try!!");

        try {
            List<LeagueEntryDto> leagueEntryDtos = this.callRiotLeague(division, tier, queue, page);

            AsyncCall<LeagueEntryDto, UserInfoDto> asyncCall = new AsyncCall<>(leagueEntryDtos);

            return asyncCall.execute(10, leagueEntryDto -> {
                SummonerDto summonerDto = this.callApiSummoner(leagueEntryDto.summonerId());
                Account account = this.callRiotAccount(summonerDto.puuid());

                return UserInfoDto.of(leagueEntryDto, summonerDto, account);
            });
        } catch (RuntimeException e) {
            throw new FailedApiCallingException(RIOT_API_MODULE_USER_INFO_FAILED);
        }
    }

    public List<LeagueEntryDto> callRiotLeague(String division, String tier, String queue, int page) {

        return restClient.get()
                .uri(UriGenerator.RIOT_LEAGUE.generate().queryParam("page", page).build(queue, tier, division))
                .accept(APPLICATION_JSON)
                .header(API_KEY, riotApiProperties.apiKey())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (RIOT_API_MODULE_LEAGUE_SUMMONER_FAILED::handler))
                .body(new ParameterizedTypeReference<>() {
                });
    }

    public Account callRiotAccount(String encryptedPuuid) {

        return restClient.get()
                .uri(UriGenerator.RIOT_ACCOUNT.generate(encryptedPuuid))
                .accept(MediaType.APPLICATION_JSON)
                .header(API_KEY, riotApiProperties.apiKey())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (RIOT_API_MODULE_ACCOUNT_FAILED::handler))
                .body(Account.class);
    }

    public SummonerDto callApiSummoner(String encryptedSummonerId) {

        return restClient.get()
                .uri(RIOT_SUMMONER.generate(encryptedSummonerId))
                .accept(APPLICATION_JSON)
                .header(API_KEY, riotApiProperties.apiKey())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (RIOT_API_MODULE_SUMMONER_FAILED::handler))
                .body(SummonerDto.class);
    }
}
