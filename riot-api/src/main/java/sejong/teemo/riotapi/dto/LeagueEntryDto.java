package sejong.teemo.riotapi.dto;

import jakarta.validation.constraints.NotNull;
import sejong.teemo.batch.entity.LeagueEntry;

/**
 * DTO for {@link LeagueEntry}
 */
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
        boolean inactive,
        @NotNull MiniSeriesDto miniSeriesDto) {
}