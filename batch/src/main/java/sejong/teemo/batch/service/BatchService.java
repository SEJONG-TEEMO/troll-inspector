package sejong.teemo.batch.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import sejong.teemo.batch.async.AsyncCall;
import sejong.teemo.batch.dto.Account;
import sejong.teemo.batch.dto.LeagueEntryDto;
import sejong.teemo.batch.dto.SummonerDto;
import sejong.teemo.batch.dto.UserInfoDto;
import sejong.teemo.batch.exception.ExceptionProvider;
import sejong.teemo.batch.exception.FailedApiCallingException;
import sejong.teemo.batch.exception.FailedRetryException;
import sejong.teemo.batch.generator.UriGenerator;
import sejong.teemo.batch.property.RiotApiProperties;

import java.util.List;

import static org.springframework.http.MediaType.*;
import static sejong.teemo.batch.generator.UriGenerator.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class BatchService {

    private final RestClient restClient;
    private final RiotApiProperties properties;

    private static final String API_KEY = "X-Riot-Token";

    private static final int DELAY = 20000;
    private static final double MULTIPLIER = 2.0;

    @Retryable(
            retryFor = FailedApiCallingException.class,
            backoff = @Backoff(delay = DELAY, multiplier = MULTIPLIER),
            recover = "returnEmptyList"
    )
    public List<UserInfoDto> callApiUserInfo(String division, String tier, String queue, int page) {
        List<LeagueEntryDto> leagueEntryDtos = this.callRiotLeague(division, tier, queue, page);

        AsyncCall<LeagueEntryDto, UserInfoDto> asyncCall = new AsyncCall<>(leagueEntryDtos);

        return asyncCall.execute(10, leagueEntryDto -> {
            SummonerDto summonerDto = this.callApiSummoner(leagueEntryDto.summonerId());
            Account account = this.callRiotAccount(summonerDto.puuid());

            return UserInfoDto.of(leagueEntryDto, summonerDto, account);
        });
    }

    @Recover
    public List<UserInfoDto> returnEmptyList(FailedApiCallingException e, String division, String tier, String queue, int page) {
        log.info("recover method execute = {}", e.getMessage());
        throw new FailedRetryException(ExceptionProvider.RETRY_FAILED);
    }

    private List<LeagueEntryDto> callRiotLeague(String division, String tier, String queue, int page) {

        return restClient.get()
                .uri(UriGenerator.RIOT_LEAGUE.generate().queryParam("page", page).build(queue, tier, division))
                .accept(APPLICATION_JSON)
                .header(API_KEY, properties.apiKey())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
                    log.info("get uri = {}", request.getURI());
                    log.error("league error status = {} message = {}", response.getStatusCode(), response.getStatusText());
                    throw new FailedApiCallingException(ExceptionProvider.RIOT_API_MODULE_LEAGUE_SUMMONER_FAILED);
                }))
                .body(new ParameterizedTypeReference<>() {});
    }

    private Account callRiotAccount(String encryptedPuuid) {

        return restClient.get()
                .uri(UriGenerator.RIOT_ACCOUNT.generate(encryptedPuuid))
                .accept(MediaType.APPLICATION_JSON)
                .header(API_KEY, properties.apiKey())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
                    log.info("get uri = {}", request.getURI());
                    log.error("league error status = {} message = {}", response.getStatusCode(), response.getStatusText());
                    throw new FailedApiCallingException(ExceptionProvider.RIOT_API_MODULE_ACCOUNT_FAILED);
                }))
                .body(Account.class);
    }

    private SummonerDto callApiSummoner(String encryptedSummonerId) {

        return restClient.get()
                .uri(RIOT_LEAGUE_SUMMONER_ID.generate(encryptedSummonerId))
                .accept(APPLICATION_JSON)
                .header(API_KEY, properties.apiKey())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
                    log.info("get uri = {}", request.getURI());
                    log.error("summoner error status = {} message = {}", response.getStatusCode(), response.getStatusText());
                    throw new FailedApiCallingException(ExceptionProvider.RIOT_API_MODULE_SUMMONER_FAILED);
                }))
                .body(SummonerDto.class);
    }
}
