package sejong.teemo.batch.exception;

import lombok.Getter;

@Getter
public class FailedApiCallingException extends RuntimeException {

    private final ExceptionProvider exceptionProvider;

    public FailedApiCallingException(ExceptionProvider exceptionProvider) {
        this.exceptionProvider = exceptionProvider;
    }

    public FailedApiCallingException(String message, ExceptionProvider exceptionProvider) {
        super(message);
        this.exceptionProvider = exceptionProvider;
    }

    public FailedApiCallingException(String message, Throwable cause, ExceptionProvider exceptionProvider) {
        super(message, cause);
        this.exceptionProvider = exceptionProvider;
    }

    public FailedApiCallingException(Throwable cause, ExceptionProvider exceptionProvider) {
        super(cause);
        this.exceptionProvider = exceptionProvider;
    }

    public FailedApiCallingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ExceptionProvider exceptionProvider) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.exceptionProvider = exceptionProvider;
    }
}
