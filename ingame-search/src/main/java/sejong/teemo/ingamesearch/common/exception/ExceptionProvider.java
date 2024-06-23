package sejong.teemo.ingamesearch.common.exception;

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

    // api 400
    RIOT_SPECTATOR_API_CALL_FAILED(NOT_FOUND, "SPECTATOR_100","SPECTATOR API 호출에 실패하였습니다."),
    RIOT_ACCOUNT_API_CALL_FAILED(NOT_FOUND, "ACCOUNT_100", "ACCOUNT API 호출에 실패하였습니다."),
    RIOT_CHAMPION_MASTERY_API_CALL_FAILED(NOT_FOUND, "CHAMPION_MASTERY_100", "CHAMPION_MASTERY API 호출에 실패하였습니다."),
    RIOT_SUMMONER_PERFORMANCE_API_CALL_FAILED(NOT_FOUND, "SUMMONER_PERFORMANCE_100", "SUMMONER PERFORMANCE API 호출에 실패하였습니다."),
    RIOT_USER_INFO_API_CALL_FAILED(NOT_FOUND, "USER_INFO_100", "USER INFO API 호출에 실패하였습니다."),
    RIOT_LEAGUE_ENTRY_API_CALL_FAILED(NOT_FOUND, "LEAGUE_ENTRY_100", "LEAGUE_ENTRY API 호출에 실패하였습니다."),
    RIOT_SUMMONER_API_CALL_FAILED(NOT_FOUND, "SUMMONER_100", "SUMMONER API 호출에 실패하였습니다."),
    RIOT_CHAMPION_IMAGE(NOT_FOUND, "CHAMPION_IMAGE_100", "CHAMPION_IMAGE 를 찾을 수 없습니다."),
    RIOT_TOO_MANY_CALLING_FAILED(TOO_MANY_REQUESTS, "TOO_MANY_100", "riot api 에 과도하게 요청되었습니다."),
    RIOT_BAD_REQUEST_CALLING_FAILED(BAD_REQUEST, "BAD_REQUEST_100", "요청 값이 잘못되었습니다."),

    // api 500
    RIOT_BAD_GATEWAY_FAILED(BAD_GATEWAY, "BAD_GATEWAY_100", "서버가 유효하지 않습니다."),
    RIOT_INTERNAL_SERVER_ERROR_FAILED(INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_100", "내부 서버의 오류가 발생하였습니다."),
    RIOT_SERVICE_AVAILABLE_FAILED(SERVICE_UNAVAILABLE, "SERVICE_AVAILABLE_100", "서비스를 이용할 수 없습니다. (Critical!!)");

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
            case TOO_MANY_REQUESTS -> throw new TooManyApiCallingException(this);
            case BAD_REQUEST -> throw new RequestFailedException(RIOT_BAD_REQUEST_CALLING_FAILED);
            case INTERNAL_SERVER_ERROR -> throw new ServerErrorException(RIOT_INTERNAL_SERVER_ERROR_FAILED);
            case SERVICE_UNAVAILABLE -> throw new ServerErrorException(RIOT_SERVICE_AVAILABLE_FAILED);
            case BAD_GATEWAY -> throw new ServerErrorException(RIOT_BAD_GATEWAY_FAILED);
            default -> throw new IllegalStateException("Unexpected value: " + response.getStatusCode());
        }
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return this.httpStatus;
    }

    @Override
    public ProblemDetail getBody() {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(this.httpStatus, this.message);
        pd.setTitle(this.code);

        return pd;
    }
}
