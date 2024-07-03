package sejong.teemo.crawling.common.exception;

import lombok.Getter;

@Getter
public enum ExceptionProvider {
    SUMMONER_NOT_FOUND(1000, "SUMMONER_001", "해당 소환사를 찾을 수 없습니다."),
    CRAWLER_FAILED(10000, "CRAWLIER_001", "크롤링에 실패하였습니다.");

    private final int statusCode;
    private final String code;
    private final String message;

    ExceptionProvider(int statusCode, String code, String message) {
        this.code = code;
        this.statusCode = statusCode;
        this.message = message;
    }

    public SummonerException getSummonerNotFoundException() {
        return new SummonerException(SUMMONER_NOT_FOUND.getMessage());
    }

    public CrawlingException getCrawlingException() {
        return new CrawlingException(CRAWLER_FAILED.getMessage());
    }
}
