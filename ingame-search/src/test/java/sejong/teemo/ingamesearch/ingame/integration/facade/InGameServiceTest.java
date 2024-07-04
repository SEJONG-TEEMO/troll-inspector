package sejong.teemo.ingamesearch.ingame.integration.facade;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sejong.teemo.ingamesearch.container.TestContainer;
import sejong.teemo.ingamesearch.presentation.dto.InGameView;
import sejong.teemo.ingamesearch.presentation.dto.user.performance.UserPerformanceDto;
import sejong.teemo.ingamesearch.domain.entity.UserInfo;
import sejong.teemo.ingamesearch.application.service.InGameService;
import sejong.teemo.ingamesearch.domain.repository.InGameRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class InGameServiceTest extends TestContainer {

    @Autowired
    private InGameService inGameService;

    @Autowired
    private InGameRepository inGameRepository;

    @Test
    void 최종_IN_GAME_통합_테스트() {
        // given
        String gameName = "a1h";
        String tag = "KR1";

        // when
        List<InGameView> inGameViews = inGameService.inGame(gameName, tag);

        // then
        System.out.println(inGameViews);
    }

    @Test
    void 최종_viewUserGamePerformance_통합_테스트() {
        // given
        String gameName = "a1h";
        String tag = "KR1";

        inGameRepository.save(UserInfo.of(
                gameName,
                tag,
                "GwKWTBNed920B3CvgGC45kzIplxjIdStCnz2Usy4iw_96FH7MlwLVigmIu_nQx_CeRgl7zu5RJp-pQ",
                "FakeSummonerId",
                "RANKED_SOLO_5x5",
                "Gold",
                "IV",
                100,
                50,
                75,
                "FakeAccountId",
                1234,
                1L,
                30L));

        // when
        UserPerformanceDto normal = inGameService.viewUserGamePerformance(gameName, tag);

        // then
        System.out.println(normal);
    }

    @Test
    void 유저_퍼포먼스_업데이트를_시도_하고_2분_안에_재시도_하면_예외_테스트() {
        // given
        String gameName = "a1h";
        String tag = "KR1";

        inGameService.viewUserGamePerformance(gameName, tag);

        inGameService.updateSummonerPerformance(gameName, tag);

        // when

        // then
        assertThatThrownBy(() -> inGameService.updateSummonerPerformance(gameName, tag))
                .isInstanceOf(IllegalArgumentException.class);
    }
}