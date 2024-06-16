package sejong.teemo.batch.generator;

import lombok.Getter;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Getter
public enum UriGenerator {

    RIOT_LEAGUE("https://kr.api.riotgames.com/lol/league-exp/v4/entries/{queue}/{tier}/{division}"),
    RIOT_SUMMONER("https://kr.api.riotgames.com/lol/summoner/v4/summoners/{encryptedSummonerId}"),
    RIOT_ACCOUNT("https://asia.api.riotgames.com/riot/account/v1/accounts/by-puuid/{puuid}");

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
