package sejong.teemo.batch.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.ErrorResponse;

import java.io.IOException;

@Getter
@Slf4j
public enum ExceptionProvider implements ErrorResponse {

    RIOT_API_MODULE_LEAGUE_SUMMONER_FAILED(HttpStatus.NOT_FOUND, "LEAGUE_SUMMONER_100", "riot api 모듈 요청에 실패하였습니다."),
    RIOT_API_MODULE_SUMMONER_FAILED(HttpStatus.NOT_FOUND, "SUMMONER_100", "riot api 모듈 요청에 실패하였습니다."),
    RIOT_API_MODULE_ACCOUNT_FAILED(HttpStatus.NOT_FOUND, "ACCOUNT_100","riot api 모듈 요청에 실패하였습니다."),
    RIOT_API_MODULE_USER_INFO_FAILED(HttpStatus.NOT_FOUND, "USER_INFO_100", "riot api 모듈 요청에 실패하였습니다."),

    TOO_MANY_CALLING_FAILED(HttpStatus.TOO_MANY_REQUESTS, "TOO_MANY_100", "riot api 에 과도하게 요청되었습니다."),
    RETRY_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "RETRY_500", "retry 에 실패 하였습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ExceptionProvider(HttpStatus httpStatus, String code, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public void handler(HttpRequest request, ClientHttpResponse response) {
        try {
            log.info("get uri = {}", request.getURI());
            log.error("account error status = {} message = {} header = {}",
                    response.getStatusCode(),
                    response.getStatusText(),
                    response.getHeaders());

            switch (response.getStatusCode()) {
                case HttpStatus.NOT_FOUND -> throw new FailedApiCallingException(this);
                case HttpStatus.TOO_MANY_REQUESTS -> throw new TooManyApiCallingException(ExceptionProvider.TOO_MANY_CALLING_FAILED);
                default -> throw new IllegalStateException("Unexpected value: " + response.getStatusCode());
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return httpStatus;
    }

    @Override
    public ProblemDetail getBody() {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(httpStatus, message);
        pd.setTitle(code);

        return pd;
    }
}
