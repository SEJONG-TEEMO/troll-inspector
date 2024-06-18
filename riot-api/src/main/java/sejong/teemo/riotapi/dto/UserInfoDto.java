package sejong.teemo.riotapi.dto;

import lombok.Builder;

/**
  link {UserDto}
 */
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
        long revisionData,
        long summonerLevel
) {

    public static UserInfoDto of(LeagueEntryDto leagueEntryDto, SummonerDto summonerDto, Account account) {
        return UserInfoDto.builder()
                .gameName(account.gameName())
                .tagLine(account.tagLine())
                .puuid(account.puuid())
                .accountId(summonerDto.accountId())
                .summonerId(leagueEntryDto.summonerId())
                .queueType(leagueEntryDto.queueType())
                .tier(leagueEntryDto.tier())
                .rank(leagueEntryDto.rank())
                .wins(leagueEntryDto.wins())
                .losses(leagueEntryDto.losses())
                .leaguePoint(leagueEntryDto.leaguePoints())
                .profileIconId(summonerDto.profileIconId())
                .revisionData(summonerDto.revisionDate())
                .summonerLevel(summonerDto.summonerLevel())
                .build();
    }
}
