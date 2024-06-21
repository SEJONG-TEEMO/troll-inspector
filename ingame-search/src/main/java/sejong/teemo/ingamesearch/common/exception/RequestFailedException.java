package sejong.teemo.ingamesearch.common.exception;

import lombok.Getter;

@Getter
public class RequestFailedException extends IllegalArgumentException {

    private final ExceptionProvider exceptionProvider;

    public RequestFailedException(ExceptionProvider exceptionProvider) {
        super(exceptionProvider.getMessage());
        this.exceptionProvider = exceptionProvider;
    }

    public RequestFailedException(String s, ExceptionProvider exceptionProvider) {
        super(s);
        this.exceptionProvider = exceptionProvider;
    }

    public RequestFailedException(String message, Throwable cause, ExceptionProvider exceptionProvider) {
        super(message, cause);
        this.exceptionProvider = exceptionProvider;
    }

    public RequestFailedException(Throwable cause, ExceptionProvider exceptionProvider) {
        super(cause);
        this.exceptionProvider = exceptionProvider;
    }
}
