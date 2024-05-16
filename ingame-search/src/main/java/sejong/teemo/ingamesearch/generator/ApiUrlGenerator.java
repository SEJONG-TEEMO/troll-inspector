package sejong.teemo.ingamesearch.generator;

import lombok.Getter;

@Getter
public enum ApiUrlGenerator {

    RIOT_SPECTATOR("/lol/spectator/v5/active-games/by-summoner/{encryptedPUUID}"),
    RIOT_ACCOUNT("/riot/account/v1/accounts/by-riot-id/{gameName}/{tagLine}");

    private final String url;

    ApiUrlGenerator(String url) {
        this.url = url;
    }

    public String generateUrl(String subUrl) {
        return this.url + subUrl;
    }

    public String generateUrl(int subUrl) {
        return this.url + subUrl;
    }

    public String generateUrl(String... subUrls) {
        StringBuilder sb = new StringBuilder();

        for(String subUrl : subUrls) {
             sb.append(subUrl);
        }

        return this.url + sb;
    }

    public String skipString(String target, String str) {
        return str.replaceAll(target, str);
    }
}
