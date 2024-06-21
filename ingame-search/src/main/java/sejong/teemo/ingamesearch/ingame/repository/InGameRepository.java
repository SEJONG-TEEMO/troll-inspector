package sejong.teemo.ingamesearch.ingame.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sejong.teemo.ingamesearch.ingame.entity.UserInfo;

import java.util.Optional;

public interface InGameRepository extends JpaRepository<UserInfo, Long> {

    boolean existsByGameNameAndTagLine(String gameName, String tagLine);

    @Query("select ui.puuid from UserInfo ui " +
            "where ui.gameName = :gameName and ui.tagLine = :tagLine")
    Optional<String> findByGameNameAndTagLine(String gameName, String tagLine);
}