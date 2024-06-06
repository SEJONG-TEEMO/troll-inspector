package sejong.teemo.crawling.mapper;

public interface CrawlerMapper<T> {

    T map(String crawlingData);
}
