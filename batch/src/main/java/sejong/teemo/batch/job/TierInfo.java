package sejong.teemo.batch.job;

import lombok.Getter;

@Getter
public enum TierInfo {
    IRON("IRON"),
    SILVER("SILVER"),
    GOLD("GOLD"),
    PLATINUM("PLATINUM"),
    EMERALD("EMERALD"),
    DIAMOND("DIAMOND"),
    MASTER("MASTER"),
    GRAND_MASTER("GRANDMASTER"),
    CHALLENGER("CHALLENGER");

    private final String value;

    TierInfo(String value) {
        this.value = value;
    }
}
