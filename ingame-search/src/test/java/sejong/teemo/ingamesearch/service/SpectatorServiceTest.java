package sejong.teemo.ingamesearch.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import sejong.teemo.ingamesearch.dto.Account;
import sejong.teemo.ingamesearch.dto.Spectator;
import sejong.teemo.ingamesearch.exception.FailedApiCallingException;
import sejong.teemo.ingamesearch.extension.TestExtension;
import sejong.teemo.ingamesearch.property.ApikeyProperties;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withResourceNotFound;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class SpectatorServiceTest extends TestExtension {

    private final MockRestServiceServer mockServer;
    private final SpectatorService spectatorService;

    public SpectatorServiceTest(@Autowired RestClient.Builder restClientBuilder,
                                @Autowired ApikeyProperties apikeyProperties) {

        this.mockServer = MockRestServiceServer.bindTo(restClientBuilder).build();
        this.spectatorService = new SpectatorService(restClientBuilder.build(), apikeyProperties);
    }

    private static final String accountUrl = "/riot/account/v1/accounts/by-riot-id/";
    private static final String spectatorUrl = "/lol/spectator/v5/active-games/by-summoner/";


    @Test
    void RIOT_API를_요청하여_ACCOUNT를_응답_받는다() throws JsonProcessingException {
        // given
        String gameName = "a1h";
        String tag = "KR1";

        mockServer.expect(requestTo(accountUrl + gameName + "/" + tag))
                .andRespond(withSuccess(getAccount(), MediaType.APPLICATION_JSON));

        // when
        Account account = spectatorService.callRiotPUUID(gameName, tag);

        // then
        assertThat(account.puuid()).isEqualTo("PUUID");
        assertThat(account.gameName()).isEqualTo(gameName);
        assertThat(account.tagLine()).isEqualTo(tag);
    }

    @Test
    void RIOT_API를_요청하여_SPETATOR를_응답_받는다() throws JsonProcessingException {
        // given
        String puuid = "PUUID";

        mockServer.expect(requestTo(spectatorUrl + puuid))
                .andRespond(withSuccess(getSpectator(), MediaType.APPLICATION_JSON));

        // when
        Spectator spectator = spectatorService.callRiotSpectatorV5(puuid);

        // then
        assertThat(spectator.gameType()).isEqualTo("type");
        assertThat(spectator.participants()).hasSize(0);
    }

    @Test
    void RIOT_API를_요청_헀을때_PUUID가_잘못될_경우_404을_응답한다() {
        // given
        String malPuuid = "MAL_PUUID";

        mockServer.expect(requestTo(spectatorUrl + malPuuid))
                .andRespond(withResourceNotFound());

        // when && then
        assertThatThrownBy(() -> spectatorService.callRiotSpectatorV5(malPuuid))
                .isInstanceOf(FailedApiCallingException.class);
    }
}