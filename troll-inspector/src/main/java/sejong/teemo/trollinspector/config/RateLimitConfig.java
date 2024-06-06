package sejong.teemo.trollinspector.config;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

import static io.github.resilience4j.ratelimiter.RateLimiterConfig.*;

@Configuration
public class RateLimitConfig {

    @Bean
    public RateLimiter rateLimiter() {
        RateLimiterConfig config = custom()
                .limitRefreshPeriod(Duration.ofSeconds(1))
                .limitForPeriod(100)
                .timeoutDuration(Duration.ofMillis(500))
                .build();
        RateLimiterRegistry registry = RateLimiterRegistry.of(config);
        return registry.rateLimiter("matchServiceLimiter");
    }
}
