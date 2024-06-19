package sejong.teemo.ingamesearch.ingame.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

/**
 */
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record LeagueEntryDto(
        @NotNull String leagueId,
        @NotNull String summonerId,
        @NotNull String queueType,
        @NotNull String tier,
        @NotNull String rank,
        int leaguePoints,
        int wins,
        int losses,
        boolean hotStreak,
        boolean veteran,
        boolean freshBlood,
        boolean inactive) {
}