package sejong.teemo.batch.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

/**
 */
@Builder
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