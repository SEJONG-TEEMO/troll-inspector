package sejong.teemo.crawling.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sejong.teemo.crawling.application.crawler.page.InGamePage;
import sejong.teemo.crawling.application.crawler.page.MatchDataPage;
import sejong.teemo.crawling.application.crawler.page.Page;
import sejong.teemo.crawling.domain.dto.InGameDto;
import sejong.teemo.crawling.domain.dto.MatchDataDto;
import sejong.teemo.crawling.common.exception.CrawlingException;
import sejong.teemo.crawling.common.generator.UrlGenerator;
import sejong.teemo.crawling.common.property.CrawlingProperties;

import java.net.URI;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CrawlerService {

    public List<MatchDataDto> crawlingMatchData(UrlGenerator url, CrawlingProperties properties, String name,
                                                String tag) {

        try {
            WebDriver webDriver = new RemoteWebDriver(new URI(properties.remoteIp()).toURL(), new FirefoxOptions());

            Page<MatchDataDto> page = new MatchDataPage();
            List<MatchDataDto> list = page.crawler(webDriver, url, name + "-" + tag);

            webDriver.close();
            return list;
        } catch (Exception e) {
            throw new CrawlingException(e.getMessage());
        }
    }

    public List<InGameDto> crawlingInGame(UrlGenerator url, CrawlingProperties properties, String name, String tag) {

        try {
            Page<InGameDto> page = new InGamePage(
                    new RemoteWebDriver(new URI(properties.remoteIp()).toURL(), new FirefoxOptions()), url);

            return page.crawler(name, tag);
        } catch (Exception e) {
            throw new CrawlingException(e.getMessage());
        }
    }
}
