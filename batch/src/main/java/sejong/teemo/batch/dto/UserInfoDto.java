package sejong.teemo.batch.dto;

import lombok.Builder;

@Builder
public record UserInfoDto(
        String gameName,
        String tagLine,
        String puuid,
        String summonerId,
        String queueType,
        String tier,
        String rank,
        int wins,
        int losses,
        int leaguePoint,
        String accountId,
        int profileIconId,
        long revisionDate,
        long summonerLevel
) {
}
