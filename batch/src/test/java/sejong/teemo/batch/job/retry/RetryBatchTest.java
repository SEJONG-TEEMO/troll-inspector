package sejong.teemo.batch.job.retry;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;
import sejong.teemo.batch.config.RestClientConfig;
import sejong.teemo.batch.config.RetryConfig;
import sejong.teemo.batch.dto.UserInfoDto;
import sejong.teemo.batch.exception.ExceptionProvider;
import sejong.teemo.batch.exception.FailedApiCallingException;
import sejong.teemo.batch.exception.FailedRetryException;
import sejong.teemo.batch.job.info.DivisionInfo;
import sejong.teemo.batch.job.info.TierInfo;
import sejong.teemo.batch.service.BatchService;

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {
        BatchService.class,
        RestClientConfig.class,
        RetryConfig.class,
})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class RetryBatchTest {

    @Autowired
    private BatchService batchService;

    @MockBean
    private RestClient restClient;

    @Test
    void 배치_서비스_RETRY_3회_실패_테스트_총_5번의_재시도_가능() {
        // given
        String division = DivisionInfo.IV.name();
        String tier = TierInfo.IRON.name();
        String queue = "RANKED_SOLO_5x5";

        int tryCount = 3;

        RestClient.RequestHeadersUriSpec requestHeadersUriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.RequestHeadersSpec requestHeadersSpec = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        given(restClient.get()).willReturn(requestHeadersUriSpec);
        given(requestHeadersUriSpec.uri(any(URI.class))).willReturn(requestHeadersSpec);
        given(requestHeadersSpec.accept(any(MediaType.class))).willReturn(requestHeadersSpec);
        given(requestHeadersSpec.retrieve()).willReturn(responseSpec);
        given(responseSpec.onStatus(any(), any())).willReturn(responseSpec);
        given(responseSpec.body(any(ParameterizedTypeReference.class)))
                .willThrow(new FailedApiCallingException(ExceptionProvider.RIOT_API_MODULE_LEAGUE_SUMMONER_FAILED))
                .willThrow(new FailedApiCallingException(ExceptionProvider.RIOT_API_MODULE_LEAGUE_SUMMONER_FAILED))
                .willReturn(List.of(userInfoDto()));

        // when
        List<UserInfoDto> userInfoDtos = batchService.callApiUserInfo(division, tier, queue, 1);

        // then
        assertThat(userInfoDtos).hasSize(1);

        then(restClient).should(times(tryCount)).get();
        then(requestHeadersUriSpec).should(times(tryCount)).uri(any(URI.class));
        then(requestHeadersSpec).should(times(tryCount)).accept(any(MediaType.class));
        then(requestHeadersSpec).should(times(tryCount)).retrieve();
        then(responseSpec).should(times(tryCount)).onStatus(any(), any());
        then(responseSpec).should(times(tryCount)).body(any(ParameterizedTypeReference.class));
    }

    @Test
    void RETRY_5회_모두_실패시_RECOVER_METHOD가_동작한다() {
        // given
        String division = DivisionInfo.IV.name();
        String tier = TierInfo.IRON.name();
        String queue = "RANKED_SOLO_5x5";

        int tryCount = 5;

        RestClient.RequestHeadersUriSpec requestHeadersUriSpec = mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.RequestHeadersSpec requestHeadersSpec = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        given(restClient.get()).willReturn(requestHeadersUriSpec);
        given(requestHeadersUriSpec.uri(any(URI.class))).willReturn(requestHeadersSpec);
        given(requestHeadersSpec.accept(any(MediaType.class))).willReturn(requestHeadersSpec);
        given(requestHeadersSpec.retrieve()).willReturn(responseSpec);
        given(responseSpec.onStatus(any(), any())).willReturn(responseSpec);
        given(responseSpec.body(any(ParameterizedTypeReference.class)))
                .willThrow(new FailedApiCallingException(ExceptionProvider.RIOT_API_MODULE_LEAGUE_SUMMONER_FAILED))
                .willThrow(new FailedApiCallingException(ExceptionProvider.RIOT_API_MODULE_LEAGUE_SUMMONER_FAILED))
                .willThrow(new FailedApiCallingException(ExceptionProvider.RIOT_API_MODULE_LEAGUE_SUMMONER_FAILED))
                .willThrow(new FailedApiCallingException(ExceptionProvider.RIOT_API_MODULE_LEAGUE_SUMMONER_FAILED))
                .willThrow(new FailedApiCallingException(ExceptionProvider.RIOT_API_MODULE_LEAGUE_SUMMONER_FAILED));

        // when
        assertThatThrownBy(() -> batchService.callApiUserInfo(division, tier, queue, 1))
                .isInstanceOf(FailedRetryException.class);

        // then

        then(restClient).should(times(tryCount)).get();
        then(requestHeadersUriSpec).should(times(tryCount)).uri(any(URI.class));
        then(requestHeadersSpec).should(times(tryCount)).accept(any(MediaType.class));
        then(requestHeadersSpec).should(times(tryCount)).retrieve();
        then(responseSpec).should(times(tryCount)).onStatus(any(), any());
        then(responseSpec).should(times(tryCount)).body(any(ParameterizedTypeReference.class));
    }

    private UserInfoDto userInfoDto() {
        return UserInfoDto.builder()
                .puuid("1")
                .accountId("1")
                .rank("2")
                .summonerId("1")
                .build();
    }
}
