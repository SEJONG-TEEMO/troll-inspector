package sejong.teemo.ingamesearch.domain.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sejong.teemo.ingamesearch.domain.entity.UserInfo;

public interface InGameRepository extends JpaRepository<UserInfo, Long> {

    boolean existsByGameNameAndTagLine(String gameName, String tagLine);

    Optional<UserInfo> findByGameNameAndTagLine(String gameName, String tagLine);

    @Query("select ui.puuid from UserInfo ui " +
            "where ui.gameName = :gameName and ui.tagLine = :tagLine")
    Optional<String> findPuuidByGameNameAndTagLine(@Param("gameName") String gameName,
                                                   @Param("tagLine") String tagLine);
}
