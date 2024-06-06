package sejong.teemo.riotapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import sejong.teemo.riotapi.dto.MatchDto;
import sejong.teemo.riotapi.dto.SummonerPerformanceRecord;
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

    public List<MatchDto> callRiotApiMatchPuuid(String puuid) {
        return restClient.get()
                .uri(UriGenerator.RIOT_MATCH_PUUID.generateUri(puuid))
                .accept(MediaType.APPLICATION_JSON)
                .header(API_KEY, riotApiProperties.apiKey())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
                    log.info("match = {}", request);
                    log.error("match riot puuid error = {}", response);
                    throw new FailedApiCallingException(ExceptionProvider.RIOT_MATCH_API_CALL_FAILED);
                })).body(new ParameterizedTypeReference<>() {});
    }

    public SummonerPerformanceRecord callRiotApiMatchMatchId(String matchId) {
        return restClient.get()
                .uri(UriGenerator.RIOT_MATCH.generateUri(matchId))
                .accept(MediaType.APPLICATION_JSON)
                .header(API_KEY, riotApiProperties.apiKey())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
                    log.info("match = {}", request);
                    log.error("match riot match error = {}", response);
                    throw new FailedApiCallingException(ExceptionProvider.RIOT_MATCH_API_CALL_FAILED);
                })).body(SummonerPerformanceRecord.class);
    }
}
