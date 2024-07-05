package sejong.teemo.riotapi.application.external;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import sejong.teemo.riotapi.common.exception.ExceptionProvider;
import sejong.teemo.riotapi.common.exception.FailedApiCallingException;
import sejong.teemo.riotapi.common.exception.NotFoundException;
import sejong.teemo.riotapi.common.generator.UriGenerator;
import sejong.teemo.riotapi.common.properties.RiotApiProperties;
import sejong.teemo.riotapi.presentation.dto.LeagueEntryDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeagueExternalApi {

    private final RestClient restClient;
    private final RiotApiProperties riotApiProperties;

    private static final String API_KEY = "X-Riot-Token";
    private static final String QUEUE = "RANKED_SOLO_5x5";

    public List<LeagueEntryDto> callRiotLeague(String division, String tier, String queue, int page) {

        return restClient.get()
                .uri(UriGenerator.RIOT_LEAGUE.generateUri().queryParam("page", page).build(queue, tier, division))
                .accept(APPLICATION_JSON)
                .header(API_KEY, riotApiProperties.apiKey())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
                    log.info("get uri = {}", request.getURI());
                    log.error("league error status = {} message = {}", response.getStatusCode(),
                            response.getStatusText());
                    throw new FailedApiCallingException(ExceptionProvider.RIOT_SPECTATOR_API_CALL_FAILED);
                }))
                .body(new ParameterizedTypeReference<>() {
                });
    }

    public LeagueEntryDto callRiotLeague(String summonerId) {

        Set<LeagueEntryDto> leagueEntryDtoSet = restClient.get()
                .uri(UriGenerator.RIOT_LEAGUE_SUMMONER_ID.generateUri(summonerId))
                .accept(APPLICATION_JSON)
                .header(API_KEY, riotApiProperties.apiKey())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
                    log.info("get uri = {}", request.getURI());
                    log.error("league error status = {} message = {}", response.getStatusCode(),
                            response.getStatusText());
                    throw new FailedApiCallingException(ExceptionProvider.RIOT_LEAGUE_API_CALL_FAILED);
                })).body(new ParameterizedTypeReference<>() {
                });

        // null safe
        return Optional.ofNullable(leagueEntryDtoSet)
                .flatMap(set -> set.stream()
                        .filter(leagueEntryDto -> Objects.equals(leagueEntryDto.queueType(), QUEUE))
                        .findFirst())
                .orElseThrow(() -> new NotFoundException(ExceptionProvider.RIOT_LEAGUE_API_CALL_FAILED));
    }
}
