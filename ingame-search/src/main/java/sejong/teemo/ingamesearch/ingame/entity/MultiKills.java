package sejong.teemo.ingamesearch.ingame.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MultiKills {

    @Column(name = "penta_kills", nullable = false)
    private int pentaKills;

    @Column(name = "quadra_kills", nullable = false)
    private int quadraKiils;

    @Column(name = "triple_kills", nullable = false)
    private int tripleKills;

    @Builder
    private MultiKills(int pentaKills, int quadraKiils, int tripleKills) {
        this.pentaKills = pentaKills;
        this.quadraKiils = quadraKiils;
        this.tripleKills = tripleKills;
    }

    public static MultiKills of(int pentaKills, int quadraKiils, int tripleKills) {
        return MultiKills.builder()
                .pentaKills(pentaKills)
                .quadraKiils(quadraKiils)
                .tripleKills(tripleKills)
                .build();
    }
}
