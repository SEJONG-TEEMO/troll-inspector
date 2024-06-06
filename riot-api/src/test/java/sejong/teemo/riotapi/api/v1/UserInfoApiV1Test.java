package sejong.teemo.riotapi.api.v1;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import sejong.teemo.riotapi.facade.UserInfoFacade;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserInfoApiV1Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserInfoFacade userInfoFacade;

    @Test
    void 소환사_통합_정보_API_응답_테스트() throws Exception {
        // given
        String division = "I";
        String tier = "DIAMOND";
        String queue = "RANKED_SOLO_5x5";

        int page = 1;

        // when
        ResultActions resultActions = mockMvc.perform(get("/teemo.gg/api/v1/user-info/{division}/{tier}/{queue}", division, tier, queue)
                .param("page", String.valueOf(page))
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }
}