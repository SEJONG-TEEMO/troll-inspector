package sejong.teemo.batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sejong.teemo.batch.entity.LeagueEntry;

public interface LeagueEntryRepository extends JpaRepository<LeagueEntry, Long> {
}
