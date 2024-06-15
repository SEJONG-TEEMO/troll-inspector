package sejong.teemo.ingamesearch.common.generator;

import lombok.Getter;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Getter
public enum UriGenerator {

    RIOT_API_LEAGUES("http://localhost:8080/teemo.gg/api/v1/league-to-summoner/{division}/{tier}/{queue}"),
    RIOT_API_LEAGUE("http://localhost:8080/teemo.gg/api/v1/league/{division}/{tier}/{queue}"),
    RIOT_API_LEAGUE_SUMMONER_ID("http://localhost:8080/teemo.gg/api/v1/league/{summonerId}"),
    RIOT_API_SUMMONER("http://localhost:8080/teemo.gg/api/v1//summoner/{encryptedSummonerId}"),
    RIOT_API_ACCOUNT("http://localhost:8080/teemo.gg/api/v1/account/{encryptedPuuid}"),
    RIOT_API_USER_INFO("http://localhost:8080/teemo.gg/api/v1/user-info/{division}/{tier}/{queue}"),
    RIOT_API_SPECTATOR("http://localhost:8080/teemo.gg/api/v1/spectator/{gameName}/{tag}"),
    RIOT_API_SUMMONER_PERFORMANCE_GAME_NAME_TAG("http://localhost:8080/teemo.gg/api/v1/match/summoner-performance/{gameName}/{tag}"),
    RIOT_API_SUMMONER_PERFORMANCE_PUUID("http://localhost:8080/teemo.gg/api/v1/match/summoner-performance/{puuid}");

    private final String url;

    UriGenerator(String url) {
        this.url = url;
    }

    public URI generate(Object... obj) {
        return UriComponentsBuilder.fromHttpUrl(this.url).build(obj);
    }

    public UriComponentsBuilder generate() {
        return UriComponentsBuilder.fromHttpUrl(this.url);
    }
}