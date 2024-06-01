package sejong.teemo.batch.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

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