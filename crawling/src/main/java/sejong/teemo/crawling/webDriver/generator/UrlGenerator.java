package sejong.teemo.crawling.webDriver.generator;

public enum UrlGenerator {

    RIOT_LEADER_BOARD("https://www.op.gg/leaderboards/tier?page="),
    RIOT_SUMMONERS("https://www.op.gg/summoners/kr/");

    private final String url;

    UrlGenerator(String url) {
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
