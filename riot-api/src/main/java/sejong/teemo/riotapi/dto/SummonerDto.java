package sejong.teemo.riotapi.dto;

import jakarta.validation.constraints.NotNull;

/**
 * DTO for {@link sejong.teemo.batch.entity.Summoner}
 */
public record SummonerDto(
        @NotNull String summonerId,
        @NotNull String accountId,
        int profileIconId,
        long revisionDate,
        @NotNull String puuid,
        long summonerLevel) {
}