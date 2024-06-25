package sejong.teemo.ingamesearch.extension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.test.context.ActiveProfiles;
import sejong.teemo.ingamesearch.ingame.dto.SpectatorDto;
import sejong.teemo.ingamesearch.ingame.dto.user.Account;
import sejong.teemo.ingamesearch.ingame.dto.user.LeagueEntryDto;
import sejong.teemo.ingamesearch.ingame.dto.user.SummonerDto;

import java.util.List;

@ActiveProfiles("test")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class TestExtension {

    private final ObjectMapper objectMapper = new ObjectMapper();

    protected String getAccount() throws JsonProcessingException {
        Account account = new Account("1234", "qwer1234", "qwer1234");

        return objectMapper.writeValueAsString(account);
    }

    protected String getSpectator() throws JsonProcessingException {
        SpectatorDto spectatorDto = SpectatorDto.of(1L, "gameType", List.of());

        return objectMapper.writeValueAsString(spectatorDto);
    }

    protected String getSummoners() throws JsonProcessingException {
        return objectMapper.writeValueAsString(List.of());
    }

    protected String getLeagueEntry() throws JsonProcessingException {

        LeagueEntryDto leagueEntryDto = LeagueEntryDto.builder()
                .leagueId("league123")
                .summonerId("summoner123")
                .queueType("RANKED_SOLO_5x5")
                .tier("GOLD")
                .rank("I")
                .leaguePoints(100)
                .wins(50)
                .losses(30)
                .hotStreak(true)
                .veteran(true)
                .freshBlood(true)
                .inactive(true)
                .build();

        return objectMapper.writeValueAsString(leagueEntryDto);
    }

    protected String getSummoner() throws JsonProcessingException {

        SummonerDto summonerDto = SummonerDto.builder()
                .id("summoner123")
                .summonerLevel(100L)
                .accountId("account123")
                .profileIconId(1)
                .puuid("1234")
                .revisionDate(100L)
                .profileIconId(100)
                .build();

        return objectMapper.writeValueAsString(summonerDto);
    }
}
