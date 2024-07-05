package sejong.teemo.ingamesearch.infrastructure.external;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import sejong.teemo.ingamesearch.common.generator.UriGenerator;
import sejong.teemo.ingamesearch.domain.dto.SpectatorDto;
import sejong.teemo.ingamesearch.domain.dto.summoner.SummonerPerformance;
import sejong.teemo.ingamesearch.domain.dto.user.Account;
import sejong.teemo.ingamesearch.domain.dto.user.LeagueEntryDto;
import sejong.teemo.ingamesearch.domain.dto.user.SummonerDto;

import java.util.List;

import static org.springframework.http.MediaType.*;
import static sejong.teemo.ingamesearch.common.exception.ExceptionProvider.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class InGameExternalApi {

    private final RestClient restClient;

    public SpectatorDto callApiSpectator(String puuid) {

        return restClient.get()
                .uri(UriGenerator.RIOT_API_SPECTATOR.generate(puuid))
                .accept(APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, RIOT_SPECTATOR_API_CALL_FAILED::handler)
                .onStatus(HttpStatusCode::is5xxServerError, RIOT_SPECTATOR_API_CALL_FAILED::handler)
                .body(SpectatorDto.class);
    }

    public List<SummonerPerformance> callRiotSummonerPerformance(String puuid) {
        return restClient.get()
                .uri(UriGenerator.RIOT_API_SUMMONER_PERFORMANCE_PUUID.generate(puuid))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, RIOT_SUMMONER_PERFORMANCE_API_CALL_FAILED::handler)
                .onStatus(HttpStatusCode::is5xxServerError, RIOT_SUMMONER_PERFORMANCE_API_CALL_FAILED::handler)
                .body(new ParameterizedTypeReference<>() {
                });
    }

    public Account callApiAccount(String gameName, String tagLine) {
        return restClient.get()
                .uri(UriGenerator.RIOT_API_ACCOUNT_GAME_NAME_AND_TAG_LINE.generate(gameName, tagLine))
                .accept(APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, RIOT_ACCOUNT_API_CALL_FAILED::handler)
                .onStatus(HttpStatusCode::is5xxServerError, RIOT_ACCOUNT_API_CALL_FAILED::handler)
                .body(Account.class);
    }

    public Account callApiAccount(String puuid) {
        return restClient.get()
                .uri(UriGenerator.RIOT_API_ACCOUNT.generate(puuid))
                .accept(APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, RIOT_ACCOUNT_API_CALL_FAILED::handler)
                .onStatus(HttpStatusCode::is5xxServerError, RIOT_ACCOUNT_API_CALL_FAILED::handler)
                .body(Account.class);
    }

    public SummonerDto callApiSummoner(String puuid) {
        return restClient.get()
                .uri(UriGenerator.RIOT_API_SUMMONER_PUUID.generate(puuid))
                .accept(APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, RIOT_SUMMONER_API_CALL_FAILED::handler)
                .onStatus(HttpStatusCode::is5xxServerError, RIOT_SUMMONER_API_CALL_FAILED::handler)
                .body(SummonerDto.class);
    }

    public LeagueEntryDto callApiLeagueEntry(String summonerId) {
        return restClient.get()
                .uri(UriGenerator.RIOT_API_LEAGUE_SUMMONER_ID.generate(summonerId))
                .accept(APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, RIOT_LEAGUE_ENTRY_API_CALL_FAILED::handler)
                .onStatus(HttpStatusCode::is5xxServerError, RIOT_LEAGUE_ENTRY_API_CALL_FAILED::handler)
                .body(LeagueEntryDto.class);
    }

    public byte[] callApiUserProfileImage(long profileId) {
        return restClient.get()
                .uri(UriGenerator.USER_PROFILE_CDN.generate(profileId))
                .accept(IMAGE_PNG)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    log.info("user profile image url = {}", request.getURI());
                    log.error("user profile image url = {}", response.getStatusText());
                    throw new IllegalArgumentException("invalid profileId" + profileId);
                }).body(byte[].class);
    }
}
