package sejong.teemo.ingamesearch.ingame.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sejong.teemo.ingamesearch.ingame.entity.SummonerPerformanceInfo;

import java.util.List;
import java.util.Optional;

public interface SummonerPerformanceInfoRepository extends JpaRepository<SummonerPerformanceInfo, Long> {

    boolean existsByUserInfoId(Long userInfoId);

    List<SummonerPerformanceInfo> findByUserInfoId(Long userInfoId);
}