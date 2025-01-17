package sejong.teemo.ingamesearch.ingame.unit.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.anyString;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sejong.teemo.ingamesearch.application.service.InGameService;
import sejong.teemo.ingamesearch.domain.entity.UserInfo;
import sejong.teemo.ingamesearch.domain.repository.InGameRepository;
import sejong.teemo.ingamesearch.domain.repository.QueryDslRepository;
import sejong.teemo.ingamesearch.domain.repository.SummonerPerformanceInfoRepository;
import sejong.teemo.ingamesearch.infrastructure.external.InGameExternalApi;
import sejong.teemo.ingamesearch.domain.dto.normal.NormalView;
import sejong.teemo.ingamesearch.domain.dto.summoner.SummonerPerformance;
import sejong.teemo.ingamesearch.domain.dto.user.Account;
import sejong.teemo.ingamesearch.domain.dto.user.UserInfoView;
import sejong.teemo.ingamesearch.domain.dto.user.performance.UserPerformanceDto;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class InGameServiceTest {

    @InjectMocks
    private InGameService inGameService;

    @Mock
    private InGameExternalApi inGameExternalApi;

    @Mock
    private InGameRepository inGameRepository;

    @Mock
    private SummonerPerformanceInfoRepository summonerPerformanceInfoRepository;

    @Mock
    private QueryDslRepository queryDslRepository;

    private static final String GAME_NAME = "a1h";
    private static final String TAG_LINE = "KR1";
    private static final String PUUID = "123e4567-e89b-12d3-a456-426614174000";

    @Test
    void 유저_이름과_태그를_입력하여_최근_전적_데이터를_조회_한다() {
        // given
        given(inGameRepository.findByGameNameAndTagLine(anyString(), anyString())).willReturn(
                Optional.ofNullable(userInfoData()));
        given(summonerPerformanceInfoRepository.existsByUserInfoId(userInfoData().getId())).willReturn(true);
        given(queryDslRepository.findRecentGamesByUserId(userInfoData().getId())).willReturn(
                viewUserGamePerformanceData());

        // when
        UserPerformanceDto performanceDto = inGameService.viewUserGamePerformance(anyString(), anyString());

        // then
        assertThat(performanceDto.userChampionPerformances()).hasSize(1);
        assertThat(performanceDto.userProfileDto().gameName()).isEqualTo(GAME_NAME);
        assertThat(performanceDto.userProfileDto().tagLine()).isEqualTo(TAG_LINE);
    }

    private List<NormalView> viewUserGamePerformanceData() {
        UserInfoView fakeUserInfoView = new UserInfoView(
                "a1h",
                "KR1",
                "DIAMOND",
                "I",
                200,
                100,
                100,
                1,
                100L
        );

        NormalView normalView = new NormalView(
                fakeUserInfoView,
                123,      // championId
                10,                 // recentGameCount
                65.0,               // winRate
                3.5,                // kda
                100,                // kills
                30,                 // deaths
                70,                 // assists
                200.0,              // cs
                2,                  // pentaKill
                4,                  // quadraKill
                6,                  // tripleKill
                7,                  // wins
                3                   // losses
        );

        return List.of(normalView);
    }

    private UserInfo userInfoData() {
        return UserInfo.of(
                GAME_NAME,
                TAG_LINE,
                PUUID,
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
                30L
        );
    }

    private List<SummonerPerformance> summonerData() {
        SummonerPerformance summonerPerformance = new SummonerPerformance(
                101, // championId
                PUUID, // puuid
                4.5, // kda
                0.75, // killParticipation
                10, // kills
                2, // deaths
                8, // assists
                200, // totalMinionsKilled
                1, // pentaKills
                2, // quadraKills
                3, // tripleKills
                true // wins
        );

        return List.of(summonerPerformance);
    }

    private Account accountData() {
        return new Account(PUUID, GAME_NAME, TAG_LINE);
    }
}
