package sejong.teemo.batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sejong.teemo.batch.entity.UserInfo;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
}