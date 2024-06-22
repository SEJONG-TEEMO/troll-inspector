package sejong.teemo.ingamesearch.ingame.select;

import lombok.Getter;

@Getter
public enum SearchMode {
    IN_GAME(1),
    NORMAL(2);

    private final int value;

    SearchMode(int value) {
        this.value = value;
    }
}
