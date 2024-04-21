package sejong.teemo.crawling.repository;

import sejong.teemo.crawling.domain.Summoner;

import java.util.List;

public interface CrawlerRepository {

    Summoner save(Summoner summoner);
    Summoner findById(Long id);
    Summoner findByNameAndTag(String name, String tag);
    void deleteById(Long id);
    Summoner update(Summoner summoner);
    List<Summoner> findAll();
    void deleteAll();
    void bulkInsert(List<Summoner> summoners);
    void bulkUpdate(List<Summoner> summoners);
}
