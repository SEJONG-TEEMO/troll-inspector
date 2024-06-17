package sejong.teemo.batch.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimiterConfig {

    private static final int CAPACITY = 100;
    private static final Long SECOND = 10L;

    @Bean
    public Bucket bucket() {
        return Bucket.builder()
                .addLimit(Bandwidth.classic(CAPACITY, Refill.intervally(CAPACITY, Duration.ofSeconds(SECOND))))
                .build();
    }
}
