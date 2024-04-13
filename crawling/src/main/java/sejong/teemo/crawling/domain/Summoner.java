package sejong.teemo.crawling.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Summoner {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String tag;

    @Column(nullable = false, length = 20)
    private String tier;

    @Column(nullable = false)
    private Long leaguePoint;

    @Column(nullable = false)
    private int level;

    @Column(nullable = false)
    private int wins;

    @Column(nullable = false)
    private int losses;

    @Builder
    public Summoner(Long id, Long leaguePoint, int level, int losses, String name, String tag, String tier, int wins) {
        this.id = id;
        this.leaguePoint = leaguePoint;
        this.level = level;
        this.losses = losses;
        this.name = name;
        this.tag = tag;
        this.tier = tier;
        this.wins = wins;
    }

    @Override
    public String toString() {
        return "Summoner{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", tag='" + tag + '\'' +
                ", tier='" + tier + '\'' +
                ", leaguePoint=" + leaguePoint +
                ", level=" + level +
                ", wins=" + wins +
                ", losses=" + losses +
                '}';
    }
}
