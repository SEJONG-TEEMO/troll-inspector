package sejong.teemo.crawling.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "summoner", indexes = @Index(name = "name_tag_idx", columnList = "name, tag"))
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

    @Column(nullable = false)
    private LocalDateTime createAt;

    @Column(nullable = false)
    private LocalDateTime updateAt;

    @Builder
    public Summoner(Long id, String name, String tag, String tier, Long leaguePoint, int level, int wins, int losses, LocalDateTime createAt, LocalDateTime updateAt) {
        this.id = id;
        this.name = name;
        this.tag = tag;
        this.tier = tier;
        this.leaguePoint = leaguePoint;
        this.level = level;
        this.wins = wins;
        this.losses = losses;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
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
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                '}';
    }
}
