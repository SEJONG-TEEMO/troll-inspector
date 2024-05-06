package sejong.teemo.crawling;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import sejong.teemo.crawling.container.TestContainer;
import sejong.teemo.crawling.dto.MatchDataDto;
import sejong.teemo.crawling.facade.CrawlerFacade;
import sejong.teemo.crawling.property.CrawlingProperties;
import sejong.teemo.crawling.property.CrawlingPropertiesV1;
import sejong.teemo.crawling.property.CrawlingPropertiesV2;
import sejong.teemo.crawling.repository.CrawlerRepository;
import sejong.teemo.crawling.service.CrawlerService;
import sejong.teemo.crawling.util.ParserUtil;
import sejong.teemo.crawling.webDriver.generator.UrlGenerator;

import java.time.Duration;
import java.util.List;
import java.util.stream.IntStream;

class CrawlerServiceTest extends TestContainer {

    private static final Logger log = LoggerFactory.getLogger(CrawlerServiceTest.class);

    @Autowired
    private CrawlerRepository crawlerRepository;

    @Autowired
    private CrawlingPropertiesV1 crawlingPropertiesV1;

    @Autowired
    private CrawlingPropertiesV2 crawlingPropertiesV2;

    @Autowired
    private List<CrawlingProperties> list;

    @Autowired
    private CrawlerService crawlerService;

    @Autowired
    private CrawlerFacade crawlerFacade;

    @Test
    void 크롤링_테스트() {

        crawlerService.crawler(UrlGenerator.RIOT_LEADER_BOARD, crawlingPropertiesV1,1, 100, 5);
    }

    @Test
    void 분산_크롤링_테스트() {
        // given
        int[] startPage = {1, 51};
        int[] endPage = {50, 100};

        // when
        crawlerFacade.distributeCrawling(startPage, endPage);

        // then

    }

    @Test
    void 매치_데이터_크롤링_데이터_테스트() {
        // given
        String name = "타 잔";
        String tag = "KR1";

        // when
        WebDriver webDriver = new FirefoxDriver(new FirefoxOptions());

        webDriver.get(UrlGenerator.RIOT_LEADER_BOARD.generateUrl(name, "-", ParserUtil.skipString("#", tag)));

        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));

        webDriver.findElement(By.cssSelector("li.css-1j5gzz7:nth-child(2)")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.css-j7qwjs:nth-child(1)")));

        List<MatchDataDto> matchDataDtos = IntStream.rangeClosed(1, 19)
                .parallel()
                .mapToObj(i -> webDriver.findElement(By.cssSelector("div.css-j7qwjs:nth-child(" + i + ")")))
                .map(WebElement::getText)
                .map(ParserUtil::parseMatchData)
                .toList();

        // then
        matchDataDtos.forEach(System.out::println);
        webDriver.close();
    }
}