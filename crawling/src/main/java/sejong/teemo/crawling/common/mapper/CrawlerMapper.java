package sejong.teemo.crawling.common.mapper;

public interface CrawlerMapper<T> {

    T map(String crawlingData);
}
