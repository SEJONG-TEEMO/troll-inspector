package sejong.teemo.crawling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class CrawlingApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrawlingApplication.class, args);
    }

}
