package sejong.teemo.ingamesearch.common.advise;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
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

    @ExceptionHandler(FailedApiCallingException.class)
    protected ErrorResponse handleFailedApiCallingException(FailedApiCallingException e) {
        return e.getExceptionProvider();
    }

    @ExceptionHandler(RequestFailedException.class)
    protected ErrorResponse handleRequestFailedException(RequestFailedException e) {
        return e.getExceptionProvider();
    }

    @ExceptionHandler(TooManyApiCallingException.class)
    protected ErrorResponse handleTooManyApiCallingException(TooManyApiCallingException e) {
        return e.getExceptionProvider();
    }

    @ExceptionHandler(ServerErrorException.class)
    protected ErrorResponse handleServerErrorException(ServerErrorException e) {
        return e.getExceptionProvider();
    }
}
