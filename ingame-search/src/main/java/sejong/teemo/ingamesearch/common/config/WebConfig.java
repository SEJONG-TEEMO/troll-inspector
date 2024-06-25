package sejong.teemo.ingamesearch.common.config;

import io.github.bucket4j.Bucket;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import sejong.teemo.ingamesearch.common.interceptor.InGameInterceptor;
import sejong.teemo.ingamesearch.common.interceptor.RateLimiterInterceptor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final Bucket bucket;

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT") // 허용할 HTTP 메서드
                .allowedHeaders("*");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new InGameInterceptor())
                .addPathPatterns("/**");

        registry.addInterceptor(new RateLimiterInterceptor(bucket))
                .addPathPatterns("/**");
    }
}
