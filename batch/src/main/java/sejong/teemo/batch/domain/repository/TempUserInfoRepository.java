package sejong.teemo.batch.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sejong.teemo.batch.domain.entity.TempUserInfo;

public interface TempUserInfoRepository extends JpaRepository<TempUserInfo, Long> {
}