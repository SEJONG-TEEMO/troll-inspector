package sejong.teemo.riotapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class RiotApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RiotApiApplication.class, args);
    }

}
