package sejong.teemo.ingamesearch.common.advise;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import sejong.teemo.ingamesearch.common.exception.FailedApiCallingException;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(FailedApiCallingException.class)
    protected ErrorResponse handleFailedApiCallingException(FailedApiCallingException e) {
        return e.getExceptionProvider();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse handleIllegalArgumentException(IllegalArgumentException e) {
        return new ErrorResponseException(HttpStatus.BAD_REQUEST, e);
    }
}
