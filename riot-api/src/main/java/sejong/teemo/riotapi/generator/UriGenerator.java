package sejong.teemo.riotapi.generator;

import lombok.Getter;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Getter
public enum UriGenerator {

    RIOT_SPECTATOR("https://kr.api.riotgames.com/lol/spectator/v5/active-games/by-summoner/{encryptedPUUID}"),
    RIOT_ACCOUNT("https://asia.api.riotgames.com/riot/account/v1/accounts/by-riot-id/{gameName}/{tag}"),
    RIOT_CHAMPION_MASTERY("https://kr.api.riotgames.com/lol/champion-mastery/v4/champion-masteries/by-puuid/{encryptedPUUID}/by-champion/{championId}"),
    RIOT_SUMMONER("https://kr.api.riotgames.com/lol/summoner/v4/summoners/{encryptedSummonerId}"),
    RIOT_LEAGUE("https://kr.api.riotgames.com/lol/league/v4/entries/{queue}/{tier}/{division}");

    private final String url;

    UriGenerator(String url) {
        this.url = url;
    }

    public URI generateUri(Object... obj) {
        return UriComponentsBuilder.fromHttpUrl(this.url).build(obj);
    }

    @Deprecated
    public URI generateUri(int page, Object... obj) {
        return UriComponentsBuilder.fromHttpUrl(this.url).queryParam("page", page).build(obj);
    }

    public UriComponentsBuilder generateUri() {
        return UriComponentsBuilder.fromHttpUrl(this.url);
    }

    public String skipString(String target, String str) {
        return str.replaceAll(target, str);
    }
}
