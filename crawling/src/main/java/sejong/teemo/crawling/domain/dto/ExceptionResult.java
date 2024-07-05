package sejong.teemo.crawling.domain.dto;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record ExceptionResult(HttpStatus status, String message) {
}
