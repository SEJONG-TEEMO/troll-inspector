package sejong.teemo.batch.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import sejong.teemo.batch.dto.UserInfoDto;
import sejong.teemo.batch.exception.ExceptionProvider;
import sejong.teemo.batch.exception.FailedApiCallingException;
import sejong.teemo.batch.exception.FailedRetryException;

import java.util.List;

import static org.springframework.http.MediaType.*;
import static sejong.teemo.batch.generator.UriGenerator.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class BatchService {

    private final RestClient restClient;

    @Retryable(
            retryFor = FailedApiCallingException.class,
            backoff = @Backoff(delay = 10000),
            recover = "returnEmptyList",
            maxAttempts = 5
    )
    public List<UserInfoDto> callApiUserInfo(String division, String tier, String queue, int page) {

        return restClient.get()
                .uri(RIOT_API_USER_INFO.generate().queryParam("page", page).build(division, tier, queue))
                .accept(APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
                    log.info("user info = {}", request.getURI());
                    log.error("user info api call error!! = {}", response.getStatusText());
                    throw new FailedApiCallingException(ExceptionProvider.RIOT_API_MODULE_USER_INFO_FAILED);
                })).body(new ParameterizedTypeReference<>() {});
    }

    @Recover
    public List<UserInfoDto> returnEmptyList(FailedApiCallingException e, String division, String tier, String queue, int page) {
        log.info("recover method execute = {}", e.getMessage());
        throw new FailedRetryException(ExceptionProvider.RETRY_FAILED);
    }
}
