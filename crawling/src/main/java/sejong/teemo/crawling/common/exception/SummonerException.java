package sejong.teemo.crawling.common.exception;

import lombok.Getter;

@Getter
public class SummonerException extends RuntimeException {

    public SummonerException() {
        super();
    }

    public SummonerException(String message) {
        super(message);
    }

    public SummonerException(String message, Throwable cause) {
        super(message, cause);
    }

    public SummonerException(Throwable cause) {
        super(cause);
    }

    protected SummonerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
