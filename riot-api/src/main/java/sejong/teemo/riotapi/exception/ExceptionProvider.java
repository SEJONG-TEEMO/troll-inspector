package sejong.teemo.riotapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;

@Getter
public enum ExceptionProvider implements ErrorResponse {

    RIOT_SPECTATOR_API_CALL_FAILED(HttpStatus.NOT_FOUND, "SPECTATOR_100","SPECTATOR API 호출에 실패하였습니다."),
    RIOT_ACCOUNT_API_CALL_FAILED(HttpStatus.NOT_FOUND, "ACCOUNT_100", "ACCOUNT API 호출에 실패하였습니다."),
    RIOT_CHAMPION_MASTERY_API_CALL_FAILED(HttpStatus.NOT_FOUND, "CHAMPION_MASTERY_100", "CHAMPION_MASTERY API 호출에 실패하였습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ExceptionProvider(HttpStatus httpStatus, String code, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
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
