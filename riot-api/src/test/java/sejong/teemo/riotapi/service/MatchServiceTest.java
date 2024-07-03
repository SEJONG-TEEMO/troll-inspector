package sejong.teemo.riotapi.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MatchServiceTest {

    @Autowired
    private MatchService matchService;

    @Test
    void 매치_테스트() {

        matchService.callRiotApiMatchPuuid("GwKWTBNed920B3CvgGC45kzIplxjIdStCnz2Usy4iw_96FH7MlwLVigmIu_nQx_CeRgl7zu5RJp-pQ");
    }
}