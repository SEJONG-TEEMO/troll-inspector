package sejong.teemo.batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sejong.teemo.batch.entity.TempUserInfo;

public interface TempUserInfoRepository extends JpaRepository<TempUserInfo, Long> {
}