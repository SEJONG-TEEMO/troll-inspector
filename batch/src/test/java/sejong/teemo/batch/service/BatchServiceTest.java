package sejong.teemo.batch.service;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sejong.teemo.batch.config.RestClientConfig;
import sejong.teemo.batch.dto.UserInfoDto;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = {
        BatchService.class,
        RestClientConfig.class
})
@ActiveProfiles("test")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class BatchServiceTest {

    @Autowired
    private BatchService batchService;

    @Test
    void 내부_라이엇_API_로_요청하여_리그_소환사_응답_값을_가져온다() {
        // given
        String division = "I";
        String tier = "DIAMOND";
        String queue = "RANKED_SOLO_5x5";

        int page = 1;

        // when
        List<UserInfoDto> leagueSummonerDtos = batchService.callApiUserInfo(division, tier, queue, page);

        // then
        assertThat(leagueSummonerDtos)
                .extracting("tier", "division")
                .contains(tuple(tier, division));
    }
}