package sejong.teemo.batch.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sejong.teemo.batch.entity.UserInfo;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void bulkInsert(List<UserInfo> list) {

        if (list.isEmpty()) return;

        String sql = "INSERT INTO user_info (game_name, tag_line, puuid, summoner_id, queue_type, tier, `rank`, wins, losses, league_point, account_id, profile_icon_id, revision_data, summoner_level) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, list.get(i).getGameName());
                ps.setString(2, list.get(i).getTagLine());
                ps.setString(3, list.get(i).getPuuid());
                ps.setString(4, list.get(i).getSummonerId());
                ps.setString(5, list.get(i).getQueueType());
                ps.setString(6, list.get(i).getTier());
                ps.setString(7, list.get(i).getRank());
                ps.setInt(8, list.get(i).getWins());
                ps.setInt(9, list.get(i).getLosses());
                ps.setInt(10, list.get(i).getLeaguePoint());
                ps.setString(11, list.get(i).getAccountId());
                ps.setInt(12, list.get(i).getProfileIconId());
                ps.setLong(13, list.get(i).getRevisionDate());
                ps.setLong(14, list.get(i).getSummonerLevel());
            }

            @Override
            public int getBatchSize() {
                return list.size();
            }
        });
    }
}
