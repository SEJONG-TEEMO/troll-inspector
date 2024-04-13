package sejong.teemo.crawling.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sejong.teemo.crawling.domain.Summoner;

public interface CrawlerRepository extends JpaRepository<Summoner, Long> {
}
