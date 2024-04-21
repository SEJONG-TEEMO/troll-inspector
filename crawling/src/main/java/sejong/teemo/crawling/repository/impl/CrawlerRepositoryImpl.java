package sejong.teemo.crawling.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sejong.teemo.crawling.domain.Summoner;
import sejong.teemo.crawling.repository.CrawlerRepository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import static sejong.teemo.crawling.domain.QSummoner.*;
import static sejong.teemo.crawling.exception.ExceptionController.*;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CrawlerRepositoryImpl implements CrawlerRepository {

    private final JdbcTemplate jdbcTemplate;
    private final JpaCrawlerRepository jpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    @Transactional
    public void bulkInsert(List<Summoner> summoners) {

        String sql = "insert into summoner (id, name, tag, tier, league_point, level, wins, losses, create_at, update_at)" +
                " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, summoners.get(i).getId());
                ps.setString(2, summoners.get(i).getName());
                ps.setString(3, summoners.get(i).getTag());
                ps.setString(4, summoners.get(i).getTier());
                ps.setLong(5, summoners.get(i).getLeaguePoint());
                ps.setInt(6, summoners.get(i).getLevel());
                ps.setInt(7, summoners.get(i).getWins());
                ps.setInt(8, summoners.get(i).getLosses());
                ps.setTimestamp(9, Timestamp.valueOf(summoners.get(i).getCreateAt()));
                ps.setTimestamp(10, Timestamp.valueOf(summoners.get(i).getUpdateAt()));
            }

            @Override
            public int getBatchSize() {
                return summoners.size();
            }
        });
    }

    @Override
    @Transactional
    public Summoner save(Summoner summoner) {
        return jpaRepository.save(summoner);
    }

    @Override
    public Summoner findById(Long id) {
        return jpaRepository.findById(id).orElseThrow(SUMMONER_NOT_FOUND::getSummonerException);
    }

    @Override
    public Summoner findByNameAndTag(String name, String tag) {
        return jpaRepository.findByNameAndTag(name, tag).orElseThrow(SUMMONER_NOT_FOUND::getSummonerException);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Summoner update(Summoner summoner) {
        return null;
    }

    @Override
    public List<Summoner> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteAll() {
        jpaRepository.deleteAll();
    }

    @Override
    @Transactional
    public void bulkUpdate(List<Summoner> summoners) {
    }
}
