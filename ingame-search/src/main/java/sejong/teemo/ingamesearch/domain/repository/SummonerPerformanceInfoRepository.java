package sejong.teemo.ingamesearch.domain.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import sejong.teemo.ingamesearch.domain.entity.SummonerPerformanceInfo;

public interface SummonerPerformanceInfoRepository extends JpaRepository<SummonerPerformanceInfo, Long> {

    boolean existsByUserInfoId(Long userInfoId);

    List<SummonerPerformanceInfo> findByUserInfoId(Long userInfoId);
}