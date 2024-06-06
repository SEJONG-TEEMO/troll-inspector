package sejong.teemo.riotapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import sejong.teemo.riotapi.dto.Account;
import sejong.teemo.riotapi.dto.Champion;
import sejong.teemo.riotapi.dto.ChampionMastery;
import sejong.teemo.riotapi.dto.Spectator;
import sejong.teemo.riotapi.exception.ExceptionProvider;
import sejong.teemo.riotapi.exception.FailedApiCallingException;
import sejong.teemo.riotapi.generator.UriGenerator;
import sejong.teemo.riotapi.properties.RiotApiProperties;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpectatorService {

    private final RestClient restClient;
    private final RiotApiProperties riotApiProperties;

    private static final String API_KEY = "X-Riot-Token";

    public Spectator callRiotSpectatorV5(String puuid) {

        log.info("[callRiotSpectatorV5]");
        log.info("puuid = {}", puuid);

        return restClient.get()
                .uri(UriGenerator.RIOT_SPECTATOR.generateUri(puuid))
                .accept(APPLICATION_JSON)
                .header(API_KEY, riotApiProperties.apiKey())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
                    log.info("get uri = {}", request.getURI());
                    log.error("spectator error status = {} message = {}", response.getStatusCode(), response.getStatusText());
                    throw new FailedApiCallingException(ExceptionProvider.RIOT_SPECTATOR_API_CALL_FAILED);
                }))
                .body(Spectator.class);
    }

    public Account callRiotPUUID(String gameName, String tag) {

        log.info("api key = {}", riotApiProperties.apiKey());

        return restClient.get()
                .uri(UriGenerator.RIOT_ACCOUNT.generateUri(gameName, tag))
                .accept(APPLICATION_JSON)
                .header(API_KEY, riotApiProperties.apiKey())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
                    log.error("account error status = {}, message = {}", response.getStatusCode(), response.getStatusText());
                    throw new FailedApiCallingException(ExceptionProvider.RIOT_ACCOUNT_API_CALL_FAILED);
                }))
                .body(Account.class);
    }

    public ChampionMastery callRiotChampionMastery(String puuid, Long championId) {

        log.info("puuid = {}, championId = {}", puuid, championId);

        return restClient.get()
                .uri(UriGenerator.RIOT_CHAMPION_MASTERY.generateUri(puuid, championId))
                .accept(APPLICATION_JSON)
                .header(API_KEY, riotApiProperties.apiKey())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
                    log.error("champion mastery error status = {}, message = {}", response.getStatusCode(), response.getStatusText());
                    throw new FailedApiCallingException(ExceptionProvider.RIOT_ACCOUNT_API_CALL_FAILED);
                }))
                .body(ChampionMastery.class);
    }

    public Champion callChampionKDA(String puuid, Long championId) {
        return null;
    }
}
