package sejong.teemo.batch.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sejong.teemo.batch.entity.TempUserInfo;
import sejong.teemo.batch.entity.UserInfo;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BatchRepositoryImpl implements BatchRepository {

    private final JdbcRepository jdbcRepository;
    private final UserInfoRepository userInfoRepository;
    private final TempUserInfoRepository tempUserInfoRepository;

    @Override
    @Transactional
    public void bulkInsert(List<UserInfo> list) {
        jdbcRepository.bulkInsert(list);
    }

    @Override
    @Transactional
    public void bulkInsertTemp(List<TempUserInfo> list) {
        jdbcRepository.bulkInsertTemp(list);
    }

    @Override
    @Transactional
    public UserInfo save(UserInfo userInfo) {
        return userInfoRepository.save(userInfo);
    }

    @Override
    public UserInfo findById(Long id) {
        return userInfoRepository.findById(id).orElseThrow();
    }

    @Override
    public List<UserInfo> findAll() {
        return userInfoRepository.findAll();
    }

    @Override
    public List<TempUserInfo> findAllTemp() {
        return tempUserInfoRepository.findAll();
    }
}
