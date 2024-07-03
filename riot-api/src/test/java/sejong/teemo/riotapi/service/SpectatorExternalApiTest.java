package sejong.teemo.riotapi.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import sejong.teemo.riotapi.dto.Account;
import sejong.teemo.riotapi.dto.LeagueEntryDto;
import sejong.teemo.riotapi.dto.Spectator;
import sejong.teemo.riotapi.dto.SummonerDto;
import sejong.teemo.riotapi.common.exception.FailedApiCallingException;
import sejong.teemo.riotapi.common.generator.UriGenerator;
import sejong.teemo.riotapi.extension.TestExtension;
import sejong.teemo.riotapi.common.properties.RiotApiProperties;
import sejong.teemo.riotapi.api.external.LeagueExternalApi;
import sejong.teemo.riotapi.api.external.SpectatorExternalApi;
import sejong.teemo.riotapi.api.external.SummonerExternalApi;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withResourceNotFound;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class SpectatorExternalApiTest extends TestExtension {

    private final MockRestServiceServer mockServer;
    private final SpectatorExternalApi spectatorExternalApi;
    private final LeagueExternalApi leagueExternalApi;
    private final SummonerExternalApi summonerExternalApi;

    public SpectatorExternalApiTest(@Autowired RestClient.Builder restClientBuilder,
                                    @Autowired RiotApiProperties riotApiProperties) {

        this.mockServer = MockRestServiceServer.bindTo(restClientBuilder).build();
        this.spectatorExternalApi = new SpectatorExternalApi(restClientBuilder.build(), riotApiProperties);
        this.leagueExternalApi = new LeagueExternalApi(restClientBuilder.build(), riotApiProperties);
        this.summonerExternalApi = new SummonerExternalApi(restClientBuilder.build(), riotApiProperties);
    }

    @Test
    void RIOT_API를_요청하여_ACCOUNT를_응답_받는다() {
        // given
        String gameName = "a1h";
        String tag = "KR1";

        mockServer.expect(requestTo(UriGenerator.RIOT_ACCOUNT.generateUri(gameName, tag)))
                .andRespond(withSuccess(getAccount(), MediaType.APPLICATION_JSON));

        // when
        Account account = spectatorExternalApi.callRiotPUUID(gameName, tag);

        // then
        assertThat(account.puuid()).isEqualTo("PUUID");
        assertThat(account.gameName()).isEqualTo(gameName);
        assertThat(account.tagLine()).isEqualTo(tag);
    }

    @Test
    void RIOT_API를_요청하여_SPETATOR를_응답_받는다() {
        // given
        String puuid = "PUUID";

        mockServer.expect(requestTo(UriGenerator.RIOT_SPECTATOR.generateUri(puuid)))
                .andRespond(withSuccess(getSpectator(), MediaType.APPLICATION_JSON));

        // when
        Spectator spectator = spectatorExternalApi.callRiotSpectatorV5(puuid);

        // then
        assertThat(spectator.gameType()).isEqualTo("");
        assertThat(spectator.participants()).hasSize(0);
    }

    @Test
    void RIOT_API를_요청_헀을때_PUUID가_잘못될_경우_404을_응답한다() {
        // given
        String malPuuid = "MAL_PUUID";

        mockServer.expect(requestTo(UriGenerator.RIOT_SPECTATOR.generateUri(malPuuid)))
                .andRespond(withResourceNotFound());

        // when && then
        assertThatThrownBy(() -> spectatorExternalApi.callRiotSpectatorV5(malPuuid))
                .isInstanceOf(FailedApiCallingException.class);
    }

    @Test
    void RIOT_API를_요청_하여_LEAGUE_ENTRY를_응답_받는다() {
        // given
        String queue = "RANKED_SOLO_5x5";
        String tier = "DIAMOND";
        String division = "I";

        int page = 1;

        mockServer.expect(requestTo(UriGenerator.RIOT_LEAGUE.generateUri().queryParam("page", page).build(queue,tier, division)))
                .andRespond(withSuccess(getLeague(), MediaType.APPLICATION_JSON));

        // when
        List<LeagueEntryDto> leagueEntryDtos = leagueExternalApi.callRiotLeague(division, tier, queue, page);

        // then
        assertThat(leagueEntryDtos).hasSize(1);
    }

    @Test
    void RIOT_API를_요청_하여_SUMMONER를_응답_받는다() {
        // given
        String encryptedPuuid = "puuid-1234-5678-91011-1213";

        mockServer.expect(requestTo(UriGenerator.RIOT_SUMMONER.generateUri(encryptedPuuid)))
                .andRespond(withSuccess(getSummoner(), MediaType.APPLICATION_JSON));

        // when
        SummonerDto summonerDto = summonerExternalApi.callApiSummoner(encryptedPuuid);

        // then
        assertThat(summonerDto.puuid()).isEqualTo(encryptedPuuid);
    }
}