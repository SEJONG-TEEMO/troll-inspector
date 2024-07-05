package sejong.teemo.ingamesearch.domain.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record SummonerDto(
        @NotNull String id,
        @NotNull String accountId,
        int profileIconId,
        long revisionDate,
        @NotNull String puuid,
        long summonerLevel) {
}