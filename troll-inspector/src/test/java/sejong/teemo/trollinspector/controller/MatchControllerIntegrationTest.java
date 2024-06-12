package sejong.teemo.trollinspector.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import sejong.teemo.trollinspector.base.BaseIntegrationTest;
import sejong.teemo.trollinspector.service.MatchService;

import java.time.Duration;
import java.util.List;

public class MatchControllerIntegrationTest extends BaseIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(MatchControllerIntegrationTest.class);

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private MatchService matchService;

    @BeforeEach
    public void setUp() {
        webTestClient = webTestClient.mutate()
                .responseTimeout(Duration.ofMillis(30000))
                .build();
    }

    @ParameterizedTest
    @DisplayName("소환사가 {소환사명} {태그명}을 입력하여 ElasticSearch에 데이터를 저장한다.")
    @CsvSource({
//            "Ogk7Of-AdYuEmjRWn_YeKvn9geL9Y1I8OWcLnvsM33-vRN2Bc6PGoUq_XK1PEIdEwFStk2lkdTodOw"
            "GwKWTBNed920B3CvgGC45kzIplxjIdStCnz2Usy4iw_96FH7MlwLVigmIu_nQx_CeRgl7zu5RJp-pQ"
    })
    public void testAnalyzeSummonerPerformance(String puuid) {

        long startTime = System.currentTimeMillis();

        webTestClient.get()
                .uri("http://localhost:8081/api/troll-inspector/v1/ids/{puuid}", puuid)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Void.class)
                .consumeWith(response -> {
                    List<?> responseBody = response.getResponseBody();
                    Assertions.assertThat(responseBody).isEmpty();
                });

        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;

        System.out.println("Response time: " + responseTime + "ms");

    }

    @ParameterizedTest
    @DisplayName("소환사가 {소환사명} {태그명}을 입력하여 ElasticSearch에 데이터를 저장한다.")
    @CsvSource({
            "뾰롱뽀롱뻐렁, KR1"
    })
    public void testServiceAnalyzeSummonerPerformance(String gameName, String tagLine) throws Exception {

        long startTime = System.currentTimeMillis();
        matchService.analyzeSummonerPerformance(gameName, tagLine);
        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;

        System.out.println("Response time: " + responseTime + "ms");
    }
}

