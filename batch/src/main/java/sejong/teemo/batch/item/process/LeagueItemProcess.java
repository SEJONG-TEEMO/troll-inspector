package sejong.teemo.batch.item.process;

import org.springframework.batch.item.ItemProcessor;
import sejong.teemo.batch.dto.UserInfoDto;
import sejong.teemo.batch.entity.UserInfo;

import java.util.List;

public class LeagueItemProcess implements ItemProcessor<List<UserInfoDto>, List<UserInfo>> {

    @Override
    public List<UserInfo> process(List<UserInfoDto> item) throws Exception {
        return item.parallelStream().map(UserInfo::from).toList();
    }
}
