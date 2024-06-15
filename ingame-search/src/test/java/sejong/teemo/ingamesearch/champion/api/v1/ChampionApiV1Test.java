package sejong.teemo.ingamesearch.champion.api.v1;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import sejong.teemo.ingamesearch.champion.service.ChampionService;
import sejong.teemo.ingamesearch.common.config.RestClientConfig;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {
        ChampionApiV1.class,
        ChampionService.class,
        RestClientConfig.class
})
@AutoConfigureMockMvc
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ChampionApiV1Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ChampionService championService;

    @Test
    void 챔피언_ID를_요청하면_챔피언_이미지가_반환된다() throws Exception {
        // given
        long championId = 1L;

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/champion/{championId}", championId));

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }
}