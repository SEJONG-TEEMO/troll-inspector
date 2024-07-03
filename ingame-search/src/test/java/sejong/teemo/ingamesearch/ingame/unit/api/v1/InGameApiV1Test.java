package sejong.teemo.ingamesearch.ingame.unit.api.v1;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import sejong.teemo.ingamesearch.common.exception.FailedApiCallingException;
import sejong.teemo.ingamesearch.common.exception.ServerErrorException;
import sejong.teemo.ingamesearch.common.exception.TooManyApiCallingException;
import sejong.teemo.ingamesearch.extension.TestExtension;
import sejong.teemo.ingamesearch.ingame.api.v1.InGameApiV1;
import sejong.teemo.ingamesearch.ingame.dto.user.performance.UserPerformanceDto;
import sejong.teemo.ingamesearch.ingame.service.InGameService;

import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InGameApiV1.class)
class InGameApiV1Test extends TestExtension {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InGameService inGameService;

    @Test
    void in_game_api_테스트_200() throws Exception {
        // given
        String gameName = "a1h";
        String tagLine = "KR1";

        given(inGameService.inGame(gameName, tagLine)).willReturn(List.of());

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/in-game")
                .param("gameName", gameName)
                .param("tagLine", tagLine));

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    void user_performance_api_테스트_200() throws Exception {
        // given
        String gameName = "a1h";
        String tagLine = "KR1";

        given(inGameService.viewUserGamePerformance(gameName, tagLine))
                .willReturn(UserPerformanceDto.builder().build());

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/performance")
                .param("gameName", gameName)
                .param("tagLine", tagLine));

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    void performance_update_api_테스트_200() throws Exception {
        // given
        String gameName = "a1h";
        String tagLine = "KR1";

        given(inGameService.updateSummonerPerformance(gameName, tagLine))
                .willReturn(UserPerformanceDto.builder().build());

        // when
        ResultActions resultActions = mockMvc.perform(patch("/api/v1/performance")
                .param("gameName", gameName)
                .param("tagLine", tagLine));

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    void in_game_예외_테스트_400() throws Exception {
        // given
        String gameName = "a1h";
        String tagLine = "KR1";

        given(inGameService.updateSummonerPerformance(gameName, tagLine))
                .willThrow(IllegalArgumentException.class);

        // when
        ResultActions resultActions = mockMvc.perform(patch("/api/v1/performance")
                .param("gameName", gameName)
                .param("tagLine", tagLine));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void in_game_예외_테스트_404() throws Exception {
        // given
        String gameName = "a1h";
        String tagLine = "KR1";

        given(inGameService.updateSummonerPerformance(gameName, tagLine))
                .willThrow(FailedApiCallingException.class);

        // when
        ResultActions resultActions = mockMvc.perform(patch("/api/v1/performance")
                .param("gameName", gameName)
                .param("tagLine", tagLine));

        // then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    void in_game_예외_테스트_429() throws Exception {
        // given
        String gameName = "a1h";
        String tagLine = "KR1";

        given(inGameService.updateSummonerPerformance(gameName, tagLine))
                .willThrow(TooManyApiCallingException.class);

        // when
        ResultActions resultActions = mockMvc.perform(patch("/api/v1/performance")
                .param("gameName", gameName)
                .param("tagLine", tagLine));

        // then
        resultActions.andExpect(status().isTooManyRequests());
    }

    @Test
    void in_game_예외_테스트_500() throws Exception {
        // given
        String gameName = "a1h";
        String tagLine = "KR1";

        given(inGameService.updateSummonerPerformance(gameName, tagLine))
                .willThrow(ServerErrorException.class);

        // when
        ResultActions resultActions = mockMvc.perform(patch("/api/v1/performance")
                .param("gameName", gameName)
                .param("tagLine", tagLine));

        // then
        resultActions.andExpect(status().isInternalServerError());
    }
}