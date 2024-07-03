package sejong.teemo.riotapi.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sejong.teemo.riotapi.api.external.MatchExternalApi;

@SpringBootTest
class MatchExternalApiTest {

    @Autowired
    private MatchExternalApi matchExternalApi;

    @Test
    void 매치_테스트() {

        matchExternalApi.callRiotApiMatchPuuid("GwKWTBNed920B3CvgGC45kzIplxjIdStCnz2Usy4iw_96FH7MlwLVigmIu_nQx_CeRgl7zu5RJp-pQ");
    }
}