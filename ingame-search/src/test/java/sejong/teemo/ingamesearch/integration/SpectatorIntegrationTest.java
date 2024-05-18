package sejong.teemo.ingamesearch.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sejong.teemo.ingamesearch.config.RestClientConfig;
import sejong.teemo.ingamesearch.dto.ChampionMastery;
import sejong.teemo.ingamesearch.exception.FailedApiCallingException;
import sejong.teemo.ingamesearch.facade.SpectatorFacade;
import sejong.teemo.ingamesearch.property.ApikeyProperties;
import sejong.teemo.ingamesearch.service.SpectatorService;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@EnableConfigurationProperties(ApikeyProperties.class)
@SpringBootTest(classes = {
        SpectatorService.class,
        SpectatorFacade.class,
        RestClientConfig.class,
})
@ActiveProfiles("test")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class SpectatorIntegrationTest {

    @Autowired
    private SpectatorFacade spectatorFacade;

    @Test
    @Disabled
    void PUUID를_획득하여_챔피언_마스터리_API를_호출하여_응답_받는다() {
        // given
        String gameName = "BLGqq2845921660";
        String tag = "BLG";

        // when
        List<ChampionMastery> championMasteries = spectatorFacade.callSpectator(gameName, tag);

        // then
        assertThat(championMasteries).hasSize(10);
    }

    @Test
    @Disabled
    void PUUID를_획득하여_챔피언_마스터리_API를_호출하여_실패한다() {
        // given
        String gameName = "흐물광오";
        String tag = "KR2";

        // when && then
        assertThatThrownBy(() -> spectatorFacade.callSpectator(gameName, tag))
                .isInstanceOf(FailedApiCallingException.class);
    }
}
