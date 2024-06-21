package sejong.teemo.ingamesearch.common.exception;

public class ServerErrorException extends RuntimeException {

    private final ExceptionProvider exceptionProvider;

    public ServerErrorException(ExceptionProvider exceptionProvider) {
        super(exceptionProvider.getMessage());
        this.exceptionProvider = exceptionProvider;
    }

    public ServerErrorException(String message, ExceptionProvider exceptionProvider) {
        super(message);
        this.exceptionProvider = exceptionProvider;
    }

    public ServerErrorException(String message, Throwable cause, ExceptionProvider exceptionProvider) {
        super(message, cause);
        this.exceptionProvider = exceptionProvider;
    }

    public ServerErrorException(Throwable cause, ExceptionProvider exceptionProvider) {
        super(cause);
        this.exceptionProvider = exceptionProvider;
    }

    public ServerErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ExceptionProvider exceptionProvider) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.exceptionProvider = exceptionProvider;
    }
}
