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
import sejong.teemo.riotapi.facade.MatchFacade;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class MatchApiV1Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MatchFacade matchFacade;

    @Test
    void 유저의_이름과_태그를_입력하여_매치_데이터를_응답_받는다() throws Exception {
        // given
        String gameName = "alh";
        String tagLine = "KR1";

        // when
        ResultActions resultActions = mockMvc.perform(get("/teemo.gg/api/v1/match/{gameName}/{tagLine}", gameName, tagLine)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }
}
