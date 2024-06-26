package sejong.teemo.ingamesearch.ingame.integration.facade;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sejong.teemo.ingamesearch.container.TestContainer;
import sejong.teemo.ingamesearch.ingame.dto.InGameView;
import sejong.teemo.ingamesearch.ingame.dto.user.performance.UserPerformanceDto;
import sejong.teemo.ingamesearch.ingame.entity.UserInfo;
import sejong.teemo.ingamesearch.ingame.facade.InGameFacade;
import sejong.teemo.ingamesearch.ingame.repository.InGameRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class InGameFacadeTest extends TestContainer {

    @Autowired
    private InGameFacade inGameFacade;

    @Autowired
    private InGameRepository inGameRepository;

    @Test
    void 최종_IN_GAME_통합_테스트() {
        // given
        String gameName = "a1h";
        String tag = "KR1";

        // when
        List<InGameView> inGameViews = inGameFacade.inGame(gameName, tag);

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
        UserPerformanceDto normal = inGameFacade.viewUserGamePerformance(gameName, tag);

        // then
        System.out.println(normal);
    }

    @Test
    void 유저_퍼포먼스_업데이트를_시도_하고_2분_안에_재시도_하면_예외_테스트() {
        // given
        String gameName = "a1h";
        String tag = "KR1";

        inGameFacade.viewUserGamePerformance(gameName, tag);

        inGameFacade.updateSummonerPerformance(gameName, tag);

        // when

        // then
        assertThatThrownBy(() -> inGameFacade.updateSummonerPerformance(gameName, tag))
                .isInstanceOf(IllegalArgumentException.class);
    }
}