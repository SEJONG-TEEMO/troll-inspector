package sejong.teemo.crawling.api.v1;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import sejong.teemo.crawling.presentation.api.v1.CrawlingApi;
import sejong.teemo.crawling.domain.dto.MatchDataDto;
import sejong.teemo.crawling.common.exception.CrawlingException;
import sejong.teemo.crawling.common.exception.ExceptionProvider;
import sejong.teemo.crawling.application.service.CrawlerService;

import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CrawlingApi.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ActiveProfiles("test")
class CrawlingApiTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CrawlerService crawlerService;

    @Test
    void 크롤링_하여_매치_데이터를_가져온다_200() throws Exception {
        // given
        String summonerName = "타 잔";
        String tag = "KR1";

        List<MatchDataDto> data = List.of(
                provideMatchDataDto(),
                provideMatchDataDto()
        );

        given(crawlerService.crawlingMatchData(any(), any(), anyString(), anyString())).willReturn(data);

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/crawling/{summoner-name}/{tag}", summonerName, tag));

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void 크롤링_하여_매치_데이터를_가져오는_것을_실패한다_500() throws Exception {
        // given
        String summonerName = "타 잔";
        String tag = "KR1";

        given(crawlerService.crawlingMatchData(any(), any(), anyString(), anyString()))
                .willThrow(CrawlingException.class);

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/crawling/{summoner-name}/{tag}", summonerName, tag));

        // then
        resultActions.andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value(ExceptionProvider.CRAWLER_FAILED.getMessage()));
    }

    private MatchDataDto provideMatchDataDto() {
        return new MatchDataDto(1, 1, 1, 1.0f, 50, 2, 2);
    }
}