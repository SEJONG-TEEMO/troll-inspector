package sejong.teemo.ingamesearch.common.exception;

import lombok.Getter;

@Getter
public class TooManyApiCallingException extends RuntimeException {

    private final ExceptionProvider exceptionProvider;

    public TooManyApiCallingException(ExceptionProvider exceptionProvider) {
        super(exceptionProvider.getMessage());
        this.exceptionProvider = exceptionProvider;
    }

    public TooManyApiCallingException(String message, ExceptionProvider exceptionProvider) {
        super(message);
        this.exceptionProvider = exceptionProvider;
    }

    public TooManyApiCallingException(String message, Throwable cause, ExceptionProvider exceptionProvider) {
        super(message, cause);
        this.exceptionProvider = exceptionProvider;
    }

    public TooManyApiCallingException(Throwable cause, ExceptionProvider exceptionProvider) {
        super(cause);
        this.exceptionProvider = exceptionProvider;
    }

    public TooManyApiCallingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ExceptionProvider exceptionProvider) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.exceptionProvider = exceptionProvider;
    }
}
