package sejong.teemo.ingamesearch.ingame.unit.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import sejong.teemo.ingamesearch.common.exception.ExceptionProvider;
import sejong.teemo.ingamesearch.common.exception.FailedApiCallingException;
import sejong.teemo.ingamesearch.common.exception.RequestFailedException;
import sejong.teemo.ingamesearch.common.exception.ServerErrorException;
import sejong.teemo.ingamesearch.common.generator.UriGenerator;
import sejong.teemo.ingamesearch.extension.TestExtension;
import sejong.teemo.ingamesearch.ingame.dto.SpectatorDto;
import sejong.teemo.ingamesearch.ingame.dto.user.Account;
import sejong.teemo.ingamesearch.ingame.service.InGameService;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@RestClientTest
class InGameServiceTest extends TestExtension {

    private final MockRestServiceServer mockServer;
    private final InGameService inGameService;

    public InGameServiceTest(@Autowired RestClient.Builder builder) {
        this.mockServer = MockRestServiceServer.bindTo(builder).build();
        this.inGameService = new InGameService(builder.build());
    }

    @Test
    void SPECTATOR_API_를_호출하여_값을_검증() throws JsonProcessingException {
        // given
        String puuid = "1234";

        mockServer.expect(requestTo(UriGenerator.RIOT_API_SPECTATOR.generate(puuid)))
                .andRespond(withSuccess(getSpectator(), MediaType.APPLICATION_JSON));

        // when
        SpectatorDto spectatorDto = inGameService.callApiSpectator(puuid);

        // then
        assertThat(spectatorDto.gameId()).isEqualTo(1L);
        assertThat(spectatorDto.gameType()).isEqualTo("gameType");
        assertThat(spectatorDto.championMasteryList()).hasSize(0);
    }

    @Test
    void ACCOUNT_API_를_호출하여_값을_검증() throws JsonProcessingException {
        // given
        String puuid = "1234";
        mockServer.expect(requestTo(UriGenerator.RIOT_API_ACCOUNT.generate(puuid)))
                .andRespond(withSuccess(getAccount(), MediaType.APPLICATION_JSON));

        // when
        Account account = inGameService.callApiAccount(puuid);

        // then
        assertThat(account.gameName()).isEqualTo("qwer1234");
        assertThat(account.tagLine()).isEqualTo("qwer1234");
        assertThat(account.puuid()).isEqualTo(puuid);
    }

    @Test
    void IN_GAME_BAD_REQUEST_예외_검증() {
        // given
        String puuid = "1234";

        mockServer.expect(requestTo(UriGenerator.RIOT_API_ACCOUNT.generate(puuid)))
                .andRespond(withBadRequest());

        // when

        // then
        assertThatThrownBy(() -> inGameService.callApiAccount(puuid))
                .isInstanceOf(RequestFailedException.class)
                .hasMessage(ExceptionProvider.RIOT_BAD_REQUEST_CALLING_FAILED.getMessage());
    }

    @Test
    void IN_GAME_NOT_FOUND_예외_검증() {
        // given
        String puuid = "1234";

        mockServer.expect(requestTo(UriGenerator.RIOT_API_ACCOUNT.generate(puuid)))
                .andRespond(withResourceNotFound());

        // when

        // then
        assertThatThrownBy(() -> inGameService.callApiAccount(puuid))
                .isInstanceOf(FailedApiCallingException.class)
                .hasMessage(ExceptionProvider.RIOT_ACCOUNT_API_CALL_FAILED.getMessage());
    }

    @Test
    void IN_GAME_FORBIDDEN_예외_검증() {
        // given
        String puuid = "1234";

        mockServer.expect(requestTo(UriGenerator.RIOT_API_ACCOUNT.generate(puuid)))
                .andRespond(withForbiddenRequest());

        // when

        // then
        assertThatThrownBy(() -> inGameService.callApiAccount(puuid))
                .isInstanceOf(FailedApiCallingException.class)
                .hasMessage(ExceptionProvider.RIOT_ACCOUNT_API_CALL_FAILED.getMessage());
    }

    @Test
    void IN_GAME_500번대_BAD_GATEWAY_예외_검증() {
        // given
        String puuid = "1234";

        mockServer.expect(requestTo(UriGenerator.RIOT_API_ACCOUNT.generate(puuid)))
                .andRespond(withBadGateway());

        // when

        // then
        assertThatThrownBy(() -> inGameService.callApiAccount(puuid))
                .isInstanceOf(ServerErrorException.class)
                .hasMessage(ExceptionProvider.RIOT_BAD_GATEWAY_FAILED.getMessage());
    }

    @Test
    void IN_GAME_500번대_INTERNAL_SERVER_ERROR_예외_검증() {
        // given
        String puuid = "1234";

        mockServer.expect(requestTo(UriGenerator.RIOT_API_ACCOUNT.generate(puuid)))
                .andRespond(withServerError());

        // when

        // then
        assertThatThrownBy(() -> inGameService.callApiAccount(puuid))
                .isInstanceOf(ServerErrorException.class)
                .hasMessage(ExceptionProvider.RIOT_INTERNAL_SERVER_ERROR_FAILED.getMessage());
    }

    @Test
    void IN_GAME_500번대_SERVICE_UNAVAILABLE_예외_검증() {
        // given
        String puuid = "1234";

        mockServer.expect(requestTo(UriGenerator.RIOT_API_ACCOUNT.generate(puuid)))
                .andRespond(withServiceUnavailable());

        // when

        // then
        assertThatThrownBy(() -> inGameService.callApiAccount(puuid))
                .isInstanceOf(ServerErrorException.class)
                .hasMessage(ExceptionProvider.RIOT_SERVICE_AVAILABLE_FAILED.getMessage());
    }
}