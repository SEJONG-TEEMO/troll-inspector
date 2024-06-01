package sejong.teemo.batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sejong.teemo.batch.entity.Summoner;

public interface SummonerRepository extends JpaRepository<Summoner, Long> {
}
