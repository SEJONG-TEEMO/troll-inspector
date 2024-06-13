package sejong.teemo.ingamesearch.ingame.api.advise;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import sejong.teemo.ingamesearch.ingame.exception.FailedApiCallingException;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(FailedApiCallingException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ErrorResponse handleFailedApiCallingException(FailedApiCallingException e) {
        return e.getExceptionProvider();
    }
}
