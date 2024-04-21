package sejong.teemo.crawling.repository.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import sejong.teemo.crawling.domain.Summoner;

import java.util.Optional;

public interface JpaCrawlerRepository extends JpaRepository<Summoner, Long>{

    Optional<Summoner> findByNameAndTag(String name, String tag);
}
