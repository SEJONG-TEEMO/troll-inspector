package sejong.teemo.batch.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import sejong.teemo.batch.entity.TempUserInfo;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TempUserInfoRowMapper implements RowMapper<TempUserInfo> {

    @Override
    public TempUserInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        return TempUserInfo.of(
                rs.getLong("id"),
                rs.getString("game_name"),
                rs.getString("tag_line"),
                rs.getString("puuid"),
                rs.getString("summoner_id"),
                rs.getString("queue_type"),
                rs.getString("tier"),
                rs.getString("rank"),
                rs.getInt("wins"),
                rs.getInt("losses"),
                rs.getInt("league_point"),
                rs.getString("account_id"),
                rs.getInt("profile_icon_id"),
                rs.getLong("revision_data"),
                rs.getLong("summoner_level")
        );
    }
}
