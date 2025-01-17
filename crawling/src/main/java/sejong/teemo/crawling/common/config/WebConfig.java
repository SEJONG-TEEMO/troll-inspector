package sejong.teemo.crawling.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173")
                .allowedOrigins("https://react-kscupel2fq-du.a.run.app/")
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 허용할 HTTP 메서드
                .allowedHeaders("*");
    }
}
