package sejong.teemo.ingamesearch.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sejong.teemo.ingamesearch.presentation.dto.user.UserInfoDto;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "user_info", indexes = {
        @Index(name = "game_name_and_tag_line_idx", columnList = "game_name, tag_line")
})
public class UserInfo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "game_name", nullable = false)
    private String gameName;

    @Column(name = "tag_line", nullable = false)
    private String tagLine;

    @Column(name = "puuid", nullable = false, length = 100)
    private String puuid;

    @Column(name = "summoner_id", nullable = false, length = 100)
    private String summonerId;

    @Column(name = "queue_type", nullable = false)
    private String queueType;

    @Column(name = "tier", nullable = false, length = 20)
    private String tier;

    @Column(name = "`rank`", nullable = false)
    private String rank;

    @Column(name = "wins", nullable = false)
    private int wins;

    @Column(name = "losses", nullable = false)
    private int losses;

    @Column(name = "league_point", nullable = false)
    private int leaguePoint;

    @Column(name = "account_id", nullable = false, length = 100)
    private String accountId;

    @Column(name = "profile_icon_id", nullable = false)
    private int profileIconId;

    @Column(name = "revision_data", nullable = false)
    private long revisionData;

    @Column(name = "summoner_level", nullable = false)
    private long summonerLevel;

    @Builder
    private UserInfo(String gameName, String tagLine, String puuid, String summonerId, String queueType, String tier, String rank, int wins, int losses, int leaguePoint, String accountId, int profileIconId, long revisionData, long summonerLevel) {
        this.gameName = gameName;
        this.tagLine = tagLine;
        this.puuid = puuid;
        this.summonerId = summonerId;
        this.queueType = queueType;
        this.tier = tier;
        this.rank = rank;
        this.wins = wins;
        this.losses = losses;
        this.leaguePoint = leaguePoint;
        this.accountId = accountId;
        this.profileIconId = profileIconId;
        this.revisionData = revisionData;
        this.summonerLevel = summonerLevel;
    }

    public static UserInfo of(String gameName, String tagLine, String puuid, String summonerId, String queueType, String tier, String rank, int wins, int losses, int leaguePoint, String accountId, int profileIconId, long revisionData, long summonerLevel) {
        return UserInfo.builder()
                .gameName(gameName)
                .tagLine(tagLine)
                .puuid(puuid)
                .summonerId(summonerId)
                .queueType(queueType)
                .tier(tier)
                .rank(rank)
                .wins(wins)
                .losses(losses)
                .leaguePoint(leaguePoint)
                .accountId(accountId)
                .profileIconId(profileIconId)
                .revisionData(revisionData)
                .summonerLevel(summonerLevel)
                .build();
    }

    public static UserInfo from(UserInfoDto userInfoDto) {
        return of(
                userInfoDto.gameName(),
                userInfoDto.tagLine(),
                userInfoDto.puuid(),
                userInfoDto.summonerId(),
                userInfoDto.queueType(),
                userInfoDto.tier(),
                userInfoDto.rank(),
                userInfoDto.wins(),
                userInfoDto.losses(),
                userInfoDto.leaguePoint(),
                userInfoDto.accountId(),
                userInfoDto.profileIconId(),
                userInfoDto.revisionData(),
                userInfoDto.summonerLevel()
        );
    }
}
