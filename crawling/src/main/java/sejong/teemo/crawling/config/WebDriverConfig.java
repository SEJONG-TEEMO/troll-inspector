package sejong.teemo.crawling.config;

import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebDriverConfig {

    @Bean
    public FirefoxOptions firefoxOptions() {
        FirefoxOptions options = new FirefoxOptions();
        //options.addArguments("--disable-popup-blocking");       //팝업안띄움
        //options.addArguments("--headless");                       //브라우저 안띄움
        //options.addArguments("--disable-gpu");			//gpu 비활성화
        //options.addArguments("--blink-settings=imagesEnabled=false"); //이미지 다운 안받음

        return options;
    }
}
