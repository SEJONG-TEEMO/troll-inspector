package sejong.teemo.crawling.exception;

import lombok.Getter;

@Getter
public class CrawlingException extends RuntimeException {

    public CrawlingException() {
        super();
    }

    public CrawlingException(String message) {
        super(message);
    }

    public CrawlingException(String message, Throwable cause) {
        super(message, cause);
    }

    public CrawlingException(Throwable cause) {
        super(cause);
    }

    protected CrawlingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
