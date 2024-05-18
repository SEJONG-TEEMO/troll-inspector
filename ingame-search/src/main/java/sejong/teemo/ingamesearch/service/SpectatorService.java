package sejong.teemo.ingamesearch.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import sejong.teemo.ingamesearch.dto.Account;
import sejong.teemo.ingamesearch.dto.ChampionMastery;
import sejong.teemo.ingamesearch.dto.Spectator;
import sejong.teemo.ingamesearch.exception.ExceptionProvider;
import sejong.teemo.ingamesearch.exception.FailedApiCallingException;
import sejong.teemo.ingamesearch.generator.ApiUrlGenerator;
import sejong.teemo.ingamesearch.property.ApikeyProperties;

import static org.springframework.http.MediaType.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpectatorService {

    private final RestClient restClient;
    private final ApikeyProperties apikeyProperties;

    private static final String API_KEY = "X-Riot-Token";

    public Spectator callRiotSpectatorV5(String puuid) {

        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(ApiUrlGenerator.RIOT_SPECTATOR.getUrl())
                        .build(puuid))
                .accept(APPLICATION_JSON)
                .header(API_KEY, apikeyProperties.apiKey())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
                    log.error("spectator error status = {} message = {}", response.getStatusCode(), response.getStatusText());
                    throw new FailedApiCallingException(ExceptionProvider.RIOT_SPECTATOR_API_CALL_FAILED);
                }))
                .body(Spectator.class);
    }

    public Account callRiotPUUID(String gameName, String tag) {

        log.info("api key = {}", apikeyProperties.apiKey());

        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(ApiUrlGenerator.RIOT_ACCOUNT.getUrl())
                        .build(gameName, tag))
                .accept(APPLICATION_JSON)
                .header(API_KEY, apikeyProperties.apiKey())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
                    log.error("account error status = {}, message = {}", response.getStatusCode(), response.getStatusText());
                    throw new FailedApiCallingException(ExceptionProvider.RIOT_ACCOUNT_API_CALL_FAILED);
                }))
                .body(Account.class);
    }

    public ChampionMastery callRiotChampionMastery(String puuid, Long championId) {

        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(ApiUrlGenerator.RIOT_CHAMPION_MASTERY.getUrl())
                        .build(puuid, championId))
                .accept(APPLICATION_JSON)
                .header(API_KEY, apikeyProperties.apiKey())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
                    log.error("champion mastery error status = {}, message = {}", response.getStatusCode(), response.getStatusText());
                    throw new FailedApiCallingException(ExceptionProvider.RIOT_ACCOUNT_API_CALL_FAILED);
                }))
                .body(ChampionMastery.class);
    }
}
