package sejong.teemo.trollinspector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("sejong.teemo.trollinspector.util")
public class TrollInspectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrollInspectorApplication.class, args);
    }

}
