package sejong.teemo.ingamesearch.common.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sejong.teemo.ingamesearch.common.type.CacheType;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static sejong.teemo.ingamesearch.common.type.CacheType.*;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public Cache<String, Boolean> cacheGameNameAndTagLine() {
        return Caffeine.newBuilder()
                .expireAfterWrite(UPDATE_PERFORMANCE.getExpireTime(), TimeUnit.SECONDS)
                .maximumSize(UPDATE_PERFORMANCE.getMaxEntry())
                .build();
    }
}
