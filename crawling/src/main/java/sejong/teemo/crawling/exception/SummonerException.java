package sejong.teemo.crawling.exception;

import lombok.Getter;

@Getter
public class SummonerException extends RuntimeException {

    private final ExceptionController exceptionController;

    public SummonerException(ExceptionController exceptionController) {
        this.exceptionController = exceptionController;
    }
}
