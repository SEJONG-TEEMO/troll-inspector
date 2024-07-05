package sejong.teemo.riotapi.infrastructure.external;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import sejong.teemo.riotapi.domain.userinfo.dto.Account;
import sejong.teemo.riotapi.common.exception.ExceptionProvider;
import sejong.teemo.riotapi.common.exception.FailedApiCallingException;
import sejong.teemo.riotapi.common.generator.UriGenerator;
import sejong.teemo.riotapi.common.properties.RiotApiProperties;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountExternalApi {

    private final RestClient restClient;
    private final RiotApiProperties riotApiProperties;

    private static final String API_KEY = "X-Riot-Token";

    public Account callRiotAccount(String encryptedPuuid) {

        return restClient.get()
                .uri(UriGenerator.RIOT_ACCOUNT.generateUri(encryptedPuuid))
                .accept(MediaType.APPLICATION_JSON)
                .header(API_KEY, riotApiProperties.apiKey())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
                    log.info("get uri = {}", request.getURI());
                    log.error("league error status = {} message = {}", response.getStatusCode(),
                            response.getStatusText());
                    throw new FailedApiCallingException(ExceptionProvider.RIOT_ACCOUNT_API_CALL_FAILED);
                }))
                .body(Account.class);
    }

    public Account callRiotAccount(String gameName, String tagLine) {

        return restClient.get()
                .uri(UriGenerator.RIOT_ACCOUNT_GAME_NAME_TAG_LINE.generateUri(gameName, tagLine))
                .accept(MediaType.APPLICATION_JSON)
                .header(API_KEY, riotApiProperties.apiKey())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
                    log.info("get uri = {}", request.getURI());
                    log.error("account error status = {} message = {}", response.getStatusCode(),
                            response.getStatusText());
                    throw new FailedApiCallingException(ExceptionProvider.RIOT_ACCOUNT_API_CALL_FAILED);
                }))
                .body(Account.class);
    }
}
