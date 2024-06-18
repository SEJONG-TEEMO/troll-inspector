package sejong.teemo.batch.repository;

import sejong.teemo.batch.entity.TempUserInfo;
import sejong.teemo.batch.entity.UserInfo;

import java.util.List;

public interface BatchRepository {

    void bulkInsert(List<UserInfo> list);
    void bulkInsertTemp(List<TempUserInfo> list);
    UserInfo findById(Long id);
    UserInfo save(UserInfo userInfo);
    List<UserInfo> findAll();
    List<TempUserInfo> findAllTemp();
}
