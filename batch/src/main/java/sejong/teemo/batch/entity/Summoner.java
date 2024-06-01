package sejong.teemo.batch.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Summoner {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "summoner_id", nullable = false, length = 63)
    private String summonerId;

    @Column(name = "account_id", nullable = false, length = 56)
    private String accountId;

    @Column(name = "profile_icon_id", nullable = false)
    private int profileIconId;

    @Column(name = "revision_date", nullable = false)
    private long revisionDate;

    @Column(name = "puuid", nullable = false, length = 78)
    private String puuid;

    @Column(name = "summoner_level", nullable = false)
    private long summonerLevel;
}
