package sejong.teemo.ingamesearch.common.type;

import lombok.Getter;

@Getter
public enum CacheType {

    UPDATE_PERFORMANCE("update_performance", 2 * 60, 1000);

    private final String name;
    private final int expireTime;
    private final int maxEntry;

    CacheType(String name, int expireTime, int maxEntry) {
        this.name = name;
        this.expireTime = expireTime;
        this.maxEntry = maxEntry;
    }
}
