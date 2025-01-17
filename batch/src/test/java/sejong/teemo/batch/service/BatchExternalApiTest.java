package sejong.teemo.batch.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withForbiddenRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withResourceNotFound;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withTooManyRequests;
import static sejong.teemo.batch.common.generator.UriGenerator.RIOT_LEAGUE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import sejong.teemo.batch.application.job.info.DivisionInfo;
import sejong.teemo.batch.application.job.info.TierInfo;
import sejong.teemo.batch.common.exception.ExceptionProvider;
import sejong.teemo.batch.common.exception.FailedApiCallingException;
import sejong.teemo.batch.common.exception.TooManyApiCallingException;
import sejong.teemo.batch.common.property.RiotApiProperties;
import sejong.teemo.batch.domain.dto.LeagueEntryDto;
import sejong.teemo.batch.domain.dto.UserInfoDto;
import sejong.teemo.batch.infrastructure.external.BatchExternalApi;

@RestClientTest
@ActiveProfiles("test")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@EnableConfigurationProperties(RiotApiProperties.class)
class BatchExternalApiTest {

    private final MockRestServiceServer mockRestServiceServer;
    private final BatchExternalApi batchExternalApi;

    public BatchExternalApiTest(@Autowired RestClient.Builder builder,
                                @Autowired RiotApiProperties riotApiProperties) {
        this.mockRestServiceServer = MockRestServiceServer.bindTo(builder).build();
        this.batchExternalApi = new BatchExternalApi(builder.build(), riotApiProperties);
    }

    @Test
    void 라이엇_API_로_요청하여_리그_응답_값을_가져온다() throws JsonProcessingException {
        // given
        String tier = TierInfo.IRON.name();
        String division = DivisionInfo.IV.name();
        String queue = "RANKED_SOLO_5x5";
        String page = "1";

        mockRestServiceServer.expect(requestTo(RIOT_LEAGUE.generate()
                        .queryParam("page", page)
                        .build(queue, tier, division)))
                .andRespond(withSuccess(getUserInfo(), MediaType.APPLICATION_JSON));

        // when
        List<LeagueEntryDto> leagueEntryDtos = batchExternalApi.callRiotLeague(division, tier, queue, 1);

        // then
        assertThat(leagueEntryDtos).hasSize(1);
        assertThat(leagueEntryDtos.getFirst().tier()).isEqualTo(tier);
        assertThat(leagueEntryDtos.getFirst().rank()).isEqualTo(division);
    }

    @Test
    void 라이엇_API를_요청하여_BAD_REQUEST_400을_받을시_예외가_터진다() {
        // given
        String tier = TierInfo.IRON.name();
        String division = DivisionInfo.IV.name();
        String queue = "RANKED_SOLO_5x5";
        String page = "1";

        mockRestServiceServer.expect(requestTo(RIOT_LEAGUE.generate()
                        .queryParam("page", page)
                        .build(queue, tier, division)))
                .andRespond(withBadRequest());

        // when

        // then
        assertThatThrownBy(() -> batchExternalApi.callRiotLeague(division, tier, queue, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("riot api 모듈 요청에 실패하였습니다.");
    }

    @Test
    void 라이엇_API를_요청하여_NOT_FOUND_404를_받을시_예외가_터진다() {
        // given
        String tier = TierInfo.IRON.name();
        String division = DivisionInfo.IV.name();
        String queue = "RANKED_SOLO_5x5";
        String page = "1";

        mockRestServiceServer.expect(requestTo(RIOT_LEAGUE.generate()
                        .queryParam("page", page)
                        .build(queue, tier, division)))
                .andRespond(withResourceNotFound());

        // when

        // then
        assertThatThrownBy(() -> batchExternalApi.callRiotLeague(division, tier, queue, 1))
                .isInstanceOf(FailedApiCallingException.class)
                .hasMessage(ExceptionProvider.RIOT_API_MODULE_LEAGUE_SUMMONER_FAILED.getMessage());
    }

    @Test
    void 라이엇_API를_요청하여_FOBBIDEN_403를_받을시_예외가_터진다() {
        // given
        String tier = TierInfo.IRON.name();
        String division = DivisionInfo.IV.name();
        String queue = "RANKED_SOLO_5x5";
        String page = "1";

        mockRestServiceServer.expect(requestTo(RIOT_LEAGUE.generate()
                        .queryParam("page", page)
                        .build(queue, tier, division)))
                .andRespond(withForbiddenRequest());

        // when

        // then
        assertThatThrownBy(() -> batchExternalApi.callRiotLeague(division, tier, queue, 1))
                .isInstanceOf(FailedApiCallingException.class)
                .hasMessage(ExceptionProvider.RIOT_API_MODULE_LEAGUE_SUMMONER_FAILED.getMessage());
    }

    @Test
    void 라이엇_API를_요청하여_TOO_MANY_REQUESTS_429를_받을시_예외가_터진다() {
        // given
        String tier = TierInfo.IRON.name();
        String division = DivisionInfo.IV.name();
        String queue = "RANKED_SOLO_5x5";
        String page = "1";

        mockRestServiceServer.expect(requestTo(RIOT_LEAGUE.generate()
                        .queryParam("page", page)
                        .build(queue, tier, division)))
                .andRespond(withTooManyRequests());

        // when

        // then
        assertThatThrownBy(() -> batchExternalApi.callRiotLeague(division, tier, queue, 1))
                .isInstanceOf(TooManyApiCallingException.class)
                .hasMessage(ExceptionProvider.TOO_MANY_CALLING_FAILED.getMessage());
    }

    private String getUserInfo() throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(List.of(UserInfoDto.builder()
                .tier("IRON")
                .rank("IV")
                .queueType("RANKED_SOLO_5x5")
                .puuid("")
                .summonerId("")
                .accountId("")
                .wins(1)
                .gameName("")
                .leaguePoint(1)
                .revisionData(1)
                .summonerLevel(1)
                .losses(1)
                .profileIconId(1)
                .tagLine("")
                .build())
        );
    }
}