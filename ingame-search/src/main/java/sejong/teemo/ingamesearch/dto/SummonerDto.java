package sejong.teemo.ingamesearch.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record SummonerDto(
        @NotNull String summonerId,
        @NotNull String accountId,
        int profileIconId,
        long revisionDate,
        @NotNull String puuid,
        long summonerLevel) {
}