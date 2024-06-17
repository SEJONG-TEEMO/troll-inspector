package sejong.teemo.batch.job.info;

import lombok.Getter;

@Getter
public enum DivisionInfo {

    I(1),
    II(2),
    III(3),
    IV(4);

    private final int value;

    DivisionInfo(int value) {
        this.value = value;
    }

    public String mapToString() {
        return switch (this) {
            case I -> "I";
            case II -> "II";
            case III -> "III";
            case IV -> "IV";
        };
    }
}
