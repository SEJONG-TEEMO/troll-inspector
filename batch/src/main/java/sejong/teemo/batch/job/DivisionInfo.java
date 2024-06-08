package sejong.teemo.batch.job;

import lombok.Getter;

@Getter
public enum DivisionInfo {
    I("I"),
    II("II"),
    III("III"),
    IV("IV");

    private final String value;

    DivisionInfo(String value) {
        this.value = value;
    }
}
