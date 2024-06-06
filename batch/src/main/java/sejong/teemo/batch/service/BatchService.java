package sejong.teemo.batch.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import sejong.teemo.batch.dto.LeagueSummonerDto;
import sejong.teemo.batch.exception.ExceptionProvider;
import sejong.teemo.batch.exception.FailedApiCallingException;

import java.util.List;

import static org.springframework.http.MediaType.*;
import static sejong.teemo.batch.generator.UriGenerator.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class BatchService {

    private final RestClient restClient;

    public List<LeagueSummonerDto> callRiotApiLeagueSummoner(String division, String tier, String queue, int page) {
        return restClient.get()
                .uri(RIOT_API_LEAGUE_SUMMONER.generate().queryParam("page", page).build(division, tier, queue))
                .accept(APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
                    log.error("Riot API league summoner call failed = {}", response);
                    throw new FailedApiCallingException(ExceptionProvider.RIOT_API_MODULE_LEAGUE_SUMMONER_FAILED);
                })).body(new ParameterizedTypeReference<>() {});
    }
}
