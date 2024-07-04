package sejong.teemo.batch.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sejong.teemo.batch.domain.entity.UserInfo;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
}