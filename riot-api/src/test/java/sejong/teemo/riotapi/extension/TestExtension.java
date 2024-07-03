package sejong.teemo.riotapi.extension;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.test.context.ActiveProfiles;
import sejong.teemo.riotapi.dto.*;
import sejong.teemo.riotapi.common.properties.RiotApiProperties;

import java.util.List;

@EnableConfigurationProperties(RiotApiProperties.class)
@ActiveProfiles("test")
@RestClientTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class TestExtension {

    @Autowired
    private ObjectMapper objectMapper;

    protected String getAccount() {
        try {
            return objectMapper.writeValueAsString(new Account("PUUID", "a1h", "KR1"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected String getSpectator() {
        try {
            return objectMapper.writeValueAsString(new Spectator(1L, "", List.of()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected String getLeague() {

        MiniSeriesDto miniSeries = MiniSeriesDto.builder()
                .losses(1)
                .progress("WLLWN")
                .target(3)
                .wins(2)
                .build();

        LeagueEntryDto leagueEntry = LeagueEntryDto.builder()
                .leagueId("league123")
                .summonerId("summoner456")
                .queueType("RANKED_SOLO_5x5")
                .tier("DIAMOND")
                .rank("I")
                .leaguePoints(100)
                .wins(20)
                .losses(10)
                .hotStreak(true)
                .veteran(false)
                .freshBlood(true)
                .inactive(false)
                .build();

        List<LeagueEntryDto> leagueEntryDtos = List.of(leagueEntry);

        try {
            return objectMapper.writeValueAsString(leagueEntryDtos);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected String getSummoner() {

        SummonerDto summoner = SummonerDto.builder()
                .id("summoner789")
                .accountId("account123")
                .profileIconId(1234)
                .revisionDate(System.currentTimeMillis())
                .puuid("puuid-1234-5678-91011-1213")
                .summonerLevel(30)
                .build();

        try {
            return objectMapper.writeValueAsString(summoner);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
