package sejong.teemo.riotapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import sejong.teemo.riotapi.dto.SummonerDto;
import sejong.teemo.riotapi.common.exception.ExceptionProvider;
import sejong.teemo.riotapi.common.exception.FailedApiCallingException;
import sejong.teemo.riotapi.common.generator.UriGenerator;
import sejong.teemo.riotapi.common.properties.RiotApiProperties;

import static org.springframework.http.MediaType.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class SummonerService {

    private final RestClient restClient;
    private final RiotApiProperties riotApiProperties;

    private static final String API_KEY = "X-Riot-Token";

    public SummonerDto callApiSummoner(String encryptedSummonerId) {

        return restClient.get()
                .uri(UriGenerator.RIOT_SUMMONER.generateUri(encryptedSummonerId))
                .accept(APPLICATION_JSON)
                .header(API_KEY, riotApiProperties.apiKey())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
                    log.info("get uri = {}", request.getURI());
                    log.error("summoner error status = {} message = {}", response.getStatusCode(), response.getStatusText());
                    throw new FailedApiCallingException(ExceptionProvider.RIOT_SUMMONER_API_CALL_FAILED);
                }))
                .body(SummonerDto.class);
    }

    public SummonerDto callApiSummonerByPuuid(String puuid) {
        return restClient.get()
                .uri(UriGenerator.RIOT_SUMMONER_PUUID.generateUri(puuid))
                .accept(APPLICATION_JSON)
                .header(API_KEY, riotApiProperties.apiKey())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
                    log.info("get uri = {}", request.getURI());
                    log.error("summoner error status = {} message = {}", response.getStatusCode(), response.getStatusText());
                    throw new FailedApiCallingException(ExceptionProvider.RIOT_SUMMONER_API_CALL_FAILED);
                }))
                .body(SummonerDto.class);
    }
}
