package sejong.teemo.ingamesearch.domain.repository;

import com.querydsl.core.types.dsl.MathExpressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sejong.teemo.ingamesearch.presentation.dto.normal.NormalView;
import sejong.teemo.ingamesearch.presentation.dto.normal.QNormalView;
import sejong.teemo.ingamesearch.presentation.dto.summoner.SummonerPerformance;
import sejong.teemo.ingamesearch.presentation.dto.user.QUserInfoView;

import java.time.LocalDateTime;
import java.util.List;

import static sejong.teemo.ingamesearch.domain.entity.QSummonerPerformanceInfo.summonerPerformanceInfo;

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
                                summonerPerformanceInfo.userInfo.profileIconId,
                                summonerPerformanceInfo.userInfo.summonerLevel
                        ),
                        summonerPerformanceInfo.championId,
                        summonerPerformanceInfo.championId.count().intValue(),
                        MathExpressions.round(getWinRate(), 2),
                        MathExpressions.round(summonerPerformanceInfo.kda.avg(), 2),
                        MathExpressions.round(summonerPerformanceInfo.kills.avg(), 2),
                        MathExpressions.round(summonerPerformanceInfo.deaths.avg(), 2),
                        MathExpressions.round(summonerPerformanceInfo.assists.avg(), 2),
                        MathExpressions.round(summonerPerformanceInfo.totalMinionKills.avg(), 2),
                        summonerPerformanceInfo.multiKills.pentaKills.sum(),
                        summonerPerformanceInfo.multiKills.quadraKiils.sum(),
                        summonerPerformanceInfo.multiKills.tripleKills.sum(),
                        getWinCount(true).intValue(),
                        getWinCount(false).intValue()
                )).from(summonerPerformanceInfo)
                .where(summonerPerformanceInfo.userInfo.id.eq(userId))
                .groupBy(summonerPerformanceInfo.championId)
                .orderBy(summonerPerformanceInfo.championId.count().desc())
                .limit(5)
                .fetch();

    }

    @Transactional
    public void updateSummonerPerformanceInfo(SummonerPerformance performances, Long userInfoId, Long performanceId) {
        queryFactory.update(summonerPerformanceInfo)
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
                .where(summonerPerformanceInfo.userInfo.id.eq(userInfoId).and(summonerPerformanceInfo.id.eq(performanceId)))
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
