package sejong.teemo.crawling.presentation.api.advise;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sejong.teemo.crawling.domain.dto.ExceptionResult;
import sejong.teemo.crawling.common.exception.CrawlingException;
import sejong.teemo.crawling.common.exception.ExceptionProvider;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler {

    @ExceptionHandler(CrawlingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ExceptionResult> handleCrawlingException(CrawlingException e) {

        log.error("crawling exception = {} ", e.getMessage());

        return ResponseEntity.internalServerError()
                .body(new ExceptionResult(HttpStatus.INTERNAL_SERVER_ERROR,
                        ExceptionProvider.CRAWLER_FAILED.getMessage()));
    }
}
