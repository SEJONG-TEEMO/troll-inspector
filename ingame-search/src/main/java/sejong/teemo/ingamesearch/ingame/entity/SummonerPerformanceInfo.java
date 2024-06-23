package sejong.teemo.ingamesearch.ingame.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sejong.teemo.ingamesearch.ingame.dto.summoner.SummonerPerformance;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "SummonerPerformanceInfo", indexes = {
        @Index(name = "idx_summonerperformanceinfo", columnList = "champion_id")
})
public class SummonerPerformanceInfo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "champion_id", nullable = false)
    private int championId;

    @Column(name = "champion_wins", nullable = false)
    private boolean championWins;

    @Column(name = "kda", nullable = false)
    private double kda;

    @Column(name = "kill_participation", nullable = false)
    private double killParticipation;

    @Column(name = "kills", nullable = false)
    private int kills;

    @Column(name = "deaths", nullable = false)
    private int deaths;

    @Column(name = "assists", nullable = false)
    private int assists;

    @Column(name = "total_minion_kills", nullable = false)
    private int totalMinionKills;

    private MultiKills multiKills;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_info_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private UserInfo userInfo;

    @Column(name = "create_at", nullable = false)
    private LocalDateTime createAt;

    @Column(name = "update_at", nullable = false)
    private LocalDateTime updateAt;

    @Builder
    private SummonerPerformanceInfo(Long id, int championId, boolean championWins, double kda, double killParticipation, int kills, int deaths, int assists, int totalMinionKills, boolean win, MultiKills multiKills, UserInfo userInfo, LocalDateTime createAt, LocalDateTime updateAt) {
        this.id = id;
        this.championId = championId;
        this.championWins = championWins;
        this.kda = kda;
        this.killParticipation = killParticipation;
        this.kills = kills;
        this.deaths = deaths;
        this.assists = assists;
        this.totalMinionKills = totalMinionKills;
        this.multiKills = multiKills;
        this.userInfo = userInfo;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public static SummonerPerformanceInfo of(int championId, boolean championWins, double kda, double killParticipation, int kills, int deaths, int assists, int totalMinionKills, boolean win, MultiKills multiKills, UserInfo userInfo, LocalDateTime createAt, LocalDateTime updateAt) {
        return SummonerPerformanceInfo.builder()
                .championId(championId)
                .championWins(championWins)
                .kda(kda)
                .killParticipation(killParticipation)
                .kills(kills)
                .deaths(deaths)
                .assists(assists)
                .totalMinionKills(totalMinionKills)
                .win(win)
                .multiKills(multiKills)
                .userInfo(userInfo)
                .createAt(createAt)
                .updateAt(updateAt)
                .build();
    }

    public static SummonerPerformanceInfo of(SummonerPerformance summonerPerformance, UserInfo userInfo) {
        return SummonerPerformanceInfo.of(
                summonerPerformance.championId(),
                summonerPerformance.win(),
                summonerPerformance.kda(),
                summonerPerformance.killParticipation(),
                summonerPerformance.kills(),
                summonerPerformance.deaths(),
                summonerPerformance.assists(),
                summonerPerformance.totalMinionsKilled(),
                summonerPerformance.win(),
                MultiKills.builder()
                        .pentaKills(summonerPerformance.pentaKills())
                        .quadraKiils(summonerPerformance.quadraKills())
                        .tripleKills(summonerPerformance.tripleKills())
                        .build(),
                userInfo,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}
