package sejong.teemo.riotapi.common.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.ErrorResponse;

import java.io.IOException;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.TOO_MANY_REQUESTS;

@Getter
@Slf4j
public enum ExceptionProvider implements ErrorResponse {

    RIOT_SPECTATOR_API_CALL_FAILED(HttpStatus.NOT_FOUND, "SPECTATOR_100","SPECTATOR API 호출에 실패하였습니다."),
    RIOT_ACCOUNT_API_CALL_FAILED(HttpStatus.NOT_FOUND, "ACCOUNT_100", "ACCOUNT API 호출에 실패하였습니다."),
    RIOT_CHAMPION_MASTERY_API_CALL_FAILED(HttpStatus.NOT_FOUND, "CHAMPION_MASTERY_100", "CHAMPION_MASTERY API 호출에 실패하였습니다."),
    RIOT_LEAGUE_API_CALL_FAILED(HttpStatus.NOT_FOUND, "LEAGUE_100", "LEAGUE API 호출에 실패하였습니다."),
    RIOT_SUMMONER_API_CALL_FAILED(HttpStatus.NOT_FOUND, "SUMMONER_100", "SUMMONER API 호출에 실패하였습니다."),
    RIOT_MATCH_API_CALL_FAILED(HttpStatus.NOT_FOUND, "MATCH_100", "MATCH API 호출에 실패하였습니다."),
    TOO_MANY_CALLING_FAILED(TOO_MANY_REQUESTS, "TOO_MANY_100", "riot api 에 과도하게 요청되었습니다."),

    NOT_FOUND_SUMMONER(HttpStatus.NOT_FOUND, "NOT_FOUND_SUMMONER_100", "소환사를 찾는데에 실패하였습니다.");

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

            exception(response);

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void exception(ClientHttpResponse response) throws IOException {
        switch (response.getStatusCode()) {
            case NOT_FOUND, FORBIDDEN -> throw new FailedApiCallingException(this);
            case BAD_REQUEST -> throw new IllegalArgumentException("riot api 모듈 요청에 실패하였습니다.");
            case TOO_MANY_REQUESTS -> throw new TooManyApiCallingException(ExceptionProvider.TOO_MANY_CALLING_FAILED);
            default -> throw new IllegalStateException("Unexpected value: " + response.getStatusCode());
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
