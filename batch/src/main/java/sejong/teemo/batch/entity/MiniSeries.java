package sejong.teemo.batch.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class MiniSeries {

    @Column(name = "losses", nullable = false)
    private int losses;

    @Column(name = "progress", nullable = false)
    private String progress;

    @Column(name = "target", nullable = false)
    private int target;

    @Column(name = "wins", nullable = false)
    private int wins;
}
