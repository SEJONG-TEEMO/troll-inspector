package sejong.teemo.riotapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import sejong.teemo.riotapi.dto.LeagueEntryDto;
import sejong.teemo.riotapi.exception.ExceptionProvider;
import sejong.teemo.riotapi.exception.FailedApiCallingException;
import sejong.teemo.riotapi.generator.UriGenerator;
import sejong.teemo.riotapi.properties.RiotApiProperties;

import java.util.List;

import static org.springframework.http.MediaType.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeagueService {

    private final RestClient restClient;
    private final RiotApiProperties riotApiProperties;

    private static final String API_KEY = "X-Riot-Token";

    public List<LeagueEntryDto> callRiotLeague(String division, String tier, String queue, int page) {

        return restClient.get()
                .uri(UriGenerator.RIOT_LEAGUE.generateUri().queryParam("page", page).build(queue, tier, division))
                .accept(APPLICATION_JSON)
                .header(API_KEY, riotApiProperties.apiKey())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
                    log.info("get uri = {}", request.getURI());
                    log.error("league error status = {} message = {}", response.getStatusCode(), response.getStatusText());
                    throw new FailedApiCallingException(ExceptionProvider.RIOT_SPECTATOR_API_CALL_FAILED);
                }))
                .body(new ParameterizedTypeReference<>() {});
    }

    public LeagueEntryDto callRiotLeague(String summonerId) {

        return restClient.get()
                .uri(UriGenerator.RIOT_LEAGUE_SUMMONER_ID.generateUri(summonerId))
                .accept(APPLICATION_JSON)
                .header(API_KEY, riotApiProperties.apiKey())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
                    log.info("get uri = {}", request.getURI());
                    log.error("league error status = {} message = {}", response.getStatusCode(), response.getStatusText());
                    throw new FailedApiCallingException(ExceptionProvider.RIOT_SPECTATOR_API_CALL_FAILED);
                })).body(LeagueEntryDto.class);
    }
}
