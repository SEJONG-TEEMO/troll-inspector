package sejong.teemo.ingamesearch.presentation.advise;

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
import sejong.teemo.ingamesearch.common.exception.RequestFailedException;
import sejong.teemo.ingamesearch.common.exception.ServerErrorException;
import sejong.teemo.ingamesearch.common.exception.TooManyApiCallingException;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ErrorResponseException(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(FailedApiCallingException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ErrorResponse handleFailedApiCallingException(FailedApiCallingException e) {
        return e.getExceptionProvider();
    }

    @ExceptionHandler(RequestFailedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponse handleRequestFailedException(RequestFailedException e) {
        return e.getExceptionProvider();
    }

    @ExceptionHandler(TooManyApiCallingException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    protected ErrorResponse handleTooManyApiCallingException(TooManyApiCallingException e) {
        return e.getExceptionProvider();
    }

    @ExceptionHandler(ServerErrorException.class)
    @ResponseStatus
    protected ErrorResponse handleServerErrorException(ServerErrorException e) {
        return e.getExceptionProvider();
    }
}
