package sejong.teemo.batch.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class LeagueEntry {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "league_id", nullable = false)
    private String leagueId;

    @Column(name = "summoner_id", nullable = false)
    private String summonerId;

    @Column(name = "queue_type", nullable = false)
    private String queueType;

    @Column(name = "tier", nullable = false)
    private String tier;

    @Column(name = "rank", nullable = false)
    private String rank;

    @Column(name = "league_points", nullable = false)
    private int leaguePoints;

    @Column(name = "wins", nullable = false)
    private int wins;

    @Column(name = "losses", nullable = false)
    private int losses;

    @Column(name = "hot_streak", nullable = false)
    private boolean hotStreak;

    @Column(name = "veteran", nullable = false)
    private boolean veteran;

    @Column(name = "fresh_blood", nullable = false)
    private boolean freshBlood;

    @Column(name = "inactive", nullable = false)
    private boolean inactive;

    private MiniSeries miniSeries;
}
