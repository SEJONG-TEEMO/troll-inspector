package sejong.teemo.ingamesearch.common.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimiterConfig {

    private static final int CAPACITY = 60;
    private static final Long MINUTE = 1L;

    @Bean
    public Bucket bucket() {
        return Bucket.builder()
                .addLimit(Bandwidth.classic(CAPACITY, Refill.intervally(CAPACITY, Duration.ofMinutes(MINUTE))))
                .build();
    }
}
