package sejong.teemo.batch.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;

@Getter
public enum ExceptionProvider implements ErrorResponse {

    RIOT_API_MODULE_LEAGUE_SUMMONER_FAILED(HttpStatus.NOT_FOUND, "LEAGUE_SUMMONER_100", "riot api 모듈 요청에 실패하였습니다.");

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
