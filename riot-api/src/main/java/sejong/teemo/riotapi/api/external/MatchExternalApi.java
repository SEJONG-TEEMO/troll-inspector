package sejong.teemo.riotapi.api.external;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import sejong.teemo.riotapi.dto.match.MatchDataDto;
import sejong.teemo.riotapi.common.exception.ExceptionProvider;
import sejong.teemo.riotapi.common.exception.FailedApiCallingException;
import sejong.teemo.riotapi.common.generator.UriGenerator;
import sejong.teemo.riotapi.common.properties.RiotApiProperties;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchExternalApi {

    private final RestClient restClient;
    private final RiotApiProperties riotApiProperties;

    private static final String API_KEY = "X-Riot-Token";

    private static final String START = "0";
    private static final String COUNT = "40";
    private static final String QUEUE = "420";

    public List<String> callRiotApiMatchPuuid(String puuid) {

        log.info("api-key = {}", riotApiProperties.apiKey());
        return restClient.get()
                .uri(UriGenerator.RIOT_MATCH_PUUID.generateUri()
                        .queryParam("start", START)
                        .queryParam("count", COUNT)
                        .queryParam("queue", QUEUE)
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

    public MatchDataDto callRiotApiMatchMatchId(String matchId) {
        return restClient.get()
                .uri(UriGenerator.RIOT_MATCH.generateUri(matchId))
                .accept(MediaType.APPLICATION_JSON)
                .header(API_KEY, riotApiProperties.apiKey())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
                    log.info("match = {}", request.getURI());
                    log.error("match riot match error = {}", response.getStatusText());
                    throw new FailedApiCallingException(ExceptionProvider.RIOT_MATCH_API_CALL_FAILED);
                })).body(MatchDataDto.class);
    }
}
