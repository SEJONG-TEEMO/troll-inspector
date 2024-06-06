package sejong.teemo.trollinspector.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sejong.teemo.trollinspector.domain.SummonerPerformance;
import sejong.teemo.trollinspector.service.MatchService;
import sejong.teemo.trollinspector.util.ConfigProperties;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
public class MatchControllerTest {

    @Autowired
    private MatchService matchService;

    @Autowired
    private ConfigProperties configProperties;

    @ParameterizedTest
    @DisplayName("소환사가 {소환사명}{태그라인}을 입력하여 ElasticSearch에 데이터를 저장한다.")
    @CsvSource({
            "뾰롱뽀롱뻐렁, KR1"
    })
    public void testAnalyzeSummonerPerformance(String gameName, String tagLine) {

        long startTime = System.currentTimeMillis();

        List<SummonerPerformance> result = null;
        try {
            result = matchService.analyzeSummonerPerformanceForPersonalKey(gameName, tagLine);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }

        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;

        assertNotNull(result);
        System.out.println("Response time: " + responseTime + "ms");

        // Additional assertions can be added here
    }
}
