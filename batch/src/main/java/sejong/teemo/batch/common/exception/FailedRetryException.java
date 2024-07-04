package sejong.teemo.batch.common.exception;

import lombok.Getter;

@Getter
public class FailedRetryException extends RuntimeException {

    private final ExceptionProvider exceptionProvider;

    public FailedRetryException(ExceptionProvider exceptionProvider) {
        this.exceptionProvider = exceptionProvider;
    }

    public FailedRetryException(String message, ExceptionProvider exceptionProvider) {
        super(message);
        this.exceptionProvider = exceptionProvider;
    }

    public FailedRetryException(String message, Throwable cause, ExceptionProvider exceptionProvider) {
        super(message, cause);
        this.exceptionProvider = exceptionProvider;
    }

    public FailedRetryException(Throwable cause, ExceptionProvider exceptionProvider) {
        super(cause);
        this.exceptionProvider = exceptionProvider;
    }

    public FailedRetryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ExceptionProvider exceptionProvider) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.exceptionProvider = exceptionProvider;
    }
}
