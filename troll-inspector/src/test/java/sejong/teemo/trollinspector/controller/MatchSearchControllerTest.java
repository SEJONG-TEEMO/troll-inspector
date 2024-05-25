package sejong.teemo.trollinspector.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sejong.teemo.trollinspector.record.GameInspectorRecord;
import sejong.teemo.trollinspector.service.PlayerStatsService;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
public class MatchSearchControllerTest {

    @Autowired
    private PlayerStatsService playerStatsService;

    @ParameterizedTest
    @DisplayName("소환사가 {소환사명}을 입력하여 ElasticSearch에서 집계한다.")
    @CsvSource({
            "a1h#KR1"
    })
    public void testAnalyzeSummonerPerformance(String gameName) {

        long startTime = System.currentTimeMillis();
        GameInspectorRecord gameInspectorRecord = null;
        try {
            gameInspectorRecord = playerStatsService.analyzePerformance(gameName);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }

        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;

        assertNotNull(gameInspectorRecord);
        System.out.println("Response time: " + responseTime + "ms");
    }
}
