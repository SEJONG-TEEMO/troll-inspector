package sejong.teemo.ingamesearch.ingame.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import sejong.teemo.ingamesearch.ingame.dto.Account;
import sejong.teemo.ingamesearch.ingame.dto.ChampionMastery;
import sejong.teemo.ingamesearch.ingame.dto.LeagueEntryDto;
import sejong.teemo.ingamesearch.ingame.dto.SummonerDto;
import sejong.teemo.ingamesearch.ingame.exception.ExceptionProvider;
import sejong.teemo.ingamesearch.ingame.exception.FailedApiCallingException;
import sejong.teemo.ingamesearch.ingame.generator.UriGenerator;

import java.util.List;

import static org.springframework.http.MediaType.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpectatorService {

    private final RestClient restClient;

    public List<ChampionMastery> callApiSpectator(String gameName, String tagLine) {
        return restClient.get()
                .uri(UriGenerator.RIOT_API_SPECTATOR.generate(gameName, tagLine))
                .accept(APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
                    log.info("riot module spectator URI = {}", request.getURI());
                    log.error("riot module spectator response = {}", response.getStatusText());
                    throw new FailedApiCallingException(ExceptionProvider.RIOT_SPECTATOR_API_CALL_FAILED);
                })).body(new ParameterizedTypeReference<>() {});
    }

    public Account callApiAccount(String puuid) {
        return restClient.get()
                .uri(UriGenerator.RIOT_API_ACCOUNT.generate(puuid))
                .accept(APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
                    log.info("riot module account URI = {}", request.getURI());
                    log.error("riot module account response = {}", response.getStatusText());
                    throw new FailedApiCallingException(ExceptionProvider.RIOT_SPECTATOR_API_CALL_FAILED);
                })).body(Account.class);
    }

    public SummonerDto callApiSummoner(String summonerId) {
        return restClient.get()
                .uri(UriGenerator.RIOT_API_SUMMONER.generate(summonerId))
                .accept(APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
                    log.info("riot module summoner URI = {}", request.getURI());
                    log.error("riot module summoner response = {}", response.getStatusText());
                    throw new FailedApiCallingException(ExceptionProvider.RIOT_SPECTATOR_API_CALL_FAILED);
                })).body(SummonerDto.class);
    }

    public LeagueEntryDto callApiLeagueEntry(String summonerId) {
        return restClient.get()
                .uri(UriGenerator.RIOT_API_LEAGUE_SUMMONER_ID.generate(summonerId))
                .accept(APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    log.info("riot module league URI = {}", request.getURI());
                    log.error("riot module league response = {}", response.getStatusText());
                    throw new FailedApiCallingException(ExceptionProvider.RIOT_SPECTATOR_API_CALL_FAILED);
                }).body(LeagueEntryDto.class);
    }
}
