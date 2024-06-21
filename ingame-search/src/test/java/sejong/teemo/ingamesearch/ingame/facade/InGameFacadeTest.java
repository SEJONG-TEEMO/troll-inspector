package sejong.teemo.ingamesearch.ingame.facade;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sejong.teemo.ingamesearch.champion.service.ChampionService;
import sejong.teemo.ingamesearch.common.config.RestClientConfig;
import sejong.teemo.ingamesearch.container.TestContainer;
import sejong.teemo.ingamesearch.ingame.dto.InGameView;
import sejong.teemo.ingamesearch.ingame.repository.InGameRepository;
import sejong.teemo.ingamesearch.ingame.service.InGameService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class InGameFacadeTest extends TestContainer {

    @Autowired
    private InGameFacade inGameFacade;

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
}