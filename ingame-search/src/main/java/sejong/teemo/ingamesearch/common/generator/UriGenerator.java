package sejong.teemo.ingamesearch.common.generator;

import lombok.Getter;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Getter
public enum UriGenerator {

    RIOT_API_LEAGUES("https://riot-api-kscupel2fq-du.a.run.app/teemo.gg/api/v1/league-to-summoner/{division}/{tier}/{queue}"),
    RIOT_API_LEAGUE("https://riot-api-kscupel2fq-du.a.run.app/teemo.gg/api/v1/league/{division}/{tier}/{queue}"),
    RIOT_API_LEAGUE_SUMMONER_ID("https://riot-api-kscupel2fq-du.a.run.app/teemo.gg/api/v1/league/{summonerId}"),
    RIOT_API_SUMMONER("https://riot-api-kscupel2fq-du.a.run.app/teemo.gg/api/v1//summoner/{encryptedSummonerId}"),
    RIOT_API_SUMMONER_PUUID("https://riot-api-kscupel2fq-du.a.run.app/teemo.gg/api/v1/summoner/by-puuid/{puuid}"),
    RIOT_API_ACCOUNT("https://riot-api-kscupel2fq-du.a.run.app/teemo.gg/api/v1/account/{encryptedPuuid}"),
    RIOT_API_ACCOUNT_GAME_NAME_AND_TAG_LINE("https://riot-api-kscupel2fq-du.a.run.app/teemo.gg/api/v1/account/{gameName}/{tagLine}"),
    RIOT_API_USER_INFO("https://riot-api-kscupel2fq-du.a.run.app/teemo.gg/api/v1/user-info/{division}/{tier}/{queue}"),
    RIOT_API_SPECTATOR("https://riot-api-kscupel2fq-du.a.run.app/teemo.gg/api/v1/spectator/{puuid}"),
    RIOT_API_SUMMONER_PERFORMANCE_GAME_NAME_TAG("https://riot-api-kscupel2fq-du.a.run.app/teemo.gg/api/v1/match/summoner-performance/{gameName}/{tag}"),
    RIOT_API_SUMMONER_PERFORMANCE_PUUID("https://riot-api-kscupel2fq-du.a.run.app/teemo.gg/api/v1/match/summoner-performance/{puuid}"),

    CHAMPION_IMAGE_CDN("https://ddragon.leagueoflegends.com/cdn/14.12.1/img/champion/{championName}.png"),

    USER_PROFILE_CDN("https://ddragon.leagueoflegends.com/cdn/14.12.1/img/profileicon/{profileId}.png");

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
