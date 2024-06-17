package sejong.teemo.batch.job.info;

import lombok.Getter;

@Getter
public enum TierInfo {
    IRON(1),
    BRONZE(2),
    SILVER(3),
    GOLD(4),
    PLATINUM(5),
    EMERALD(6),
    DIAMOND(7),
    MASTER(8),
    GRAND_MASTER(9),
    CHALLENGER(10);

    private final int value;

    TierInfo(int value) {
        this.value = value;
    }

    public String mapToString() {
        return switch (this) {
            case IRON -> "IRON";
            case BRONZE -> "BRONZE";
            case SILVER -> "SILVER";
            case GOLD -> "GOLD";
            case PLATINUM -> "PLATINUM";
            case EMERALD -> "EMERALD";
            case DIAMOND -> "DIAMOND";
            case MASTER -> "MASTER";
            case GRAND_MASTER -> "GRANDMASTER";
            case CHALLENGER -> "CHALLENGER";
        };
    }
}
