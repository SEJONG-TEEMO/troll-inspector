package sejong.teemo.ingamesearch.ingame.repository;

import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sejong.teemo.ingamesearch.ingame.dto.normal.NormalView;
import sejong.teemo.ingamesearch.ingame.dto.normal.QNormalView;
import sejong.teemo.ingamesearch.ingame.dto.summoner.SummonerPerformance;
import sejong.teemo.ingamesearch.ingame.dto.user.QUserInfoView;

import java.time.LocalDateTime;
import java.util.List;

import static sejong.teemo.ingamesearch.ingame.entity.QSummonerPerformanceInfo.*;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QueryDslRepository {

    private final JPAQueryFactory queryFactory;

    public List<NormalView> findRecentGamesByUserId(Long userId) {
        return queryFactory.select(
                new QNormalView(
                        new QUserInfoView(
                                summonerPerformanceInfo.userInfo.gameName,
                                summonerPerformanceInfo.userInfo.tagLine,
                                summonerPerformanceInfo.userInfo.tier,
                                summonerPerformanceInfo.userInfo.rank,
                                summonerPerformanceInfo.userInfo.wins
                                        .add(summonerPerformanceInfo.userInfo.losses),
                                summonerPerformanceInfo.userInfo.wins,
                                summonerPerformanceInfo.userInfo.losses,
                                summonerPerformanceInfo.userInfo.profileIconId
                        ),
                        summonerPerformanceInfo.championId,
                        summonerPerformanceInfo.championId.count().intValue(),
                        getWinRate(),
                        summonerPerformanceInfo.kda.avg(),
                        summonerPerformanceInfo.kills.avg(),
                        summonerPerformanceInfo.deaths.avg(),
                        summonerPerformanceInfo.assists.avg(),
                        summonerPerformanceInfo.totalMinionKills.avg(),
                        summonerPerformanceInfo.multiKills.pentaKills.sum(),
                        summonerPerformanceInfo.multiKills.quadraKiils.sum(),
                        summonerPerformanceInfo.multiKills.tripleKills.sum(),
                        getWinCount(true).intValue(),
                        getWinCount(false).intValue()
                )).from(summonerPerformanceInfo)
                .where(summonerPerformanceInfo.userInfo.id.eq(userId))
                .groupBy(summonerPerformanceInfo.championId)
                .orderBy(summonerPerformanceInfo.championId.count().desc())
                .fetch();

    }

    @Transactional
    public long updateSummonerPerformanceInfo(SummonerPerformance performances, Long userInfoId) {
        return queryFactory.update(summonerPerformanceInfo)
                .set(summonerPerformanceInfo.championId, performances.championId())
                .set(summonerPerformanceInfo.kda, performances.kda())
                .set(summonerPerformanceInfo.killParticipation, performances.killParticipation())
                .set(summonerPerformanceInfo.kills, performances.kills())
                .set(summonerPerformanceInfo.deaths, performances.deaths())
                .set(summonerPerformanceInfo.assists, performances.assists())
                .set(summonerPerformanceInfo.totalMinionKills, performances.totalMinionsKilled())
                .set(summonerPerformanceInfo.multiKills.pentaKills, performances.pentaKills())
                .set(summonerPerformanceInfo.multiKills.quadraKiils, performances.quadraKills())
                .set(summonerPerformanceInfo.multiKills.tripleKills, performances.tripleKills())
                .set(summonerPerformanceInfo.championWins, performances.win())
                .set(summonerPerformanceInfo.updateAt, LocalDateTime.now())
                .where(summonerPerformanceInfo.userInfo.id.eq(userInfoId))
                .execute();
    }

    private static NumberExpression<Integer> getWinCount(boolean check) {
        return summonerPerformanceInfo.championWins.when(check).then(1).otherwise(0).sum();
    }

    private static NumberExpression<Double> getWinRate() {
        return getWinCount(true).doubleValue()
                .divide(summonerPerformanceInfo.championId.count().doubleValue()).multiply(100);
    }
}
