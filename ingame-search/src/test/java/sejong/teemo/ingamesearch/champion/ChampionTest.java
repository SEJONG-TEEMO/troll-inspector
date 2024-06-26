package sejong.teemo.ingamesearch.champion;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sejong.teemo.ingamesearch.common.config.ChampionConfig;

import java.util.Map;

@SpringBootTest(classes = ChampionConfig.class)
public class ChampionTest {

    @Autowired
    private Map<Integer, String> mapToChampions;

    @Test
    void testChampion() {
        String championName = mapToChampions.get(145);

        Assertions.assertThat(championName).isEqualTo("Kaisa");
    }
}
