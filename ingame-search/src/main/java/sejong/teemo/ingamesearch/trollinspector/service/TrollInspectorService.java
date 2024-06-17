package sejong.teemo.ingamesearch.trollinspector.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import sejong.teemo.ingamesearch.common.exception.ExceptionProvider;
import sejong.teemo.ingamesearch.common.exception.FailedApiCallingException;
import sejong.teemo.ingamesearch.common.generator.UriGenerator;
import sejong.teemo.ingamesearch.trollinspector.dto.SummonerPerformance;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrollInspectorService {

    private final RestClient restClient;

    public List<SummonerPerformance> callRiotSummonerPerformance(String puuid) {
        return restClient.get()
                .uri(UriGenerator.RIOT_API_SUMMONER_PERFORMANCE_PUUID.generate(puuid))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    log.info("riot summoner performance = {}", request.getURI());
                    log.error("riot summoner performance error = {}", response.getStatusText());
                    throw new FailedApiCallingException(ExceptionProvider.RIOT_SUMMONER_PERFORMANCE_API_CALL_FAILED);
                }).body(new ParameterizedTypeReference<>() {});
    }
}
