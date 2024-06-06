package sejong.teemo.riotapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import sejong.teemo.riotapi.exception.ExceptionProvider;
import sejong.teemo.riotapi.exception.FailedApiCallingException;
import sejong.teemo.riotapi.generator.UriGenerator;
import sejong.teemo.riotapi.properties.RiotApiProperties;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchService {

    private final RestClient restClient;
    private final RiotApiProperties riotApiProperties;

    private static final String API_KEY = "X-Riot-Token";

    public List<String> callRiotApiMatchPuuid(String puuid) {
        return restClient.get()
                .uri(UriGenerator.RIOT_MATCH_PUUID.generateUri()
                        .queryParam("start", String.valueOf(0))
                        .queryParam("count", String.valueOf(20))
                        .build(puuid))
                .accept(MediaType.APPLICATION_JSON)
                .header(API_KEY, riotApiProperties.apiKey())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
                    log.info("match = {}", request.getURI());
                    log.error("match riot puuid error = {}", response.getStatusText());
                    throw new FailedApiCallingException(ExceptionProvider.RIOT_MATCH_API_CALL_FAILED);
                })).body(new ParameterizedTypeReference<>() {});
    }

    public String callRiotApiMatchMatchId(String matchId) {
        return restClient.get()
                .uri(UriGenerator.RIOT_MATCH.generateUri(matchId))
                .accept(MediaType.APPLICATION_JSON)
                .header(API_KEY, riotApiProperties.apiKey())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
                    log.info("match = {}", request.getURI());
                    log.error("match riot match error = {}", response.getStatusText());
                    throw new FailedApiCallingException(ExceptionProvider.RIOT_MATCH_API_CALL_FAILED);
                })).body(String.class);
    }
}
