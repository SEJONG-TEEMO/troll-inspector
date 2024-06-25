package sejong.teemo.riotapi.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {

    private final ExceptionProvider exceptionProvider;

    public NotFoundException(ExceptionProvider exceptionProvider) {
        super(exceptionProvider.getMessage());
        this.exceptionProvider = exceptionProvider;
    }

    public NotFoundException(String message, ExceptionProvider exceptionProvider) {
        super(message);
        this.exceptionProvider = exceptionProvider;
    }

    public NotFoundException(String message, Throwable cause, ExceptionProvider exceptionProvider) {
        super(message, cause);
        this.exceptionProvider = exceptionProvider;
    }

    public NotFoundException(Throwable cause, ExceptionProvider exceptionProvider) {
        super(cause);
        this.exceptionProvider = exceptionProvider;
    }

    public NotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ExceptionProvider exceptionProvider) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.exceptionProvider = exceptionProvider;
    }
}
