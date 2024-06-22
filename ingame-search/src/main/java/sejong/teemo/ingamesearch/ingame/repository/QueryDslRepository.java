package sejong.teemo.ingamesearch.ingame.repository;

import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sejong.teemo.ingamesearch.ingame.dto.normal.NormalView;
import sejong.teemo.ingamesearch.ingame.dto.normal.QNormalView;
import sejong.teemo.ingamesearch.ingame.dto.user.QUserInfoView;

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
                                summonerPerformanceInfo.userInfo.tier,
                                summonerPerformanceInfo.userInfo.rank,
                                summonerPerformanceInfo.userInfo.wins,
                                summonerPerformanceInfo.userInfo.losses,
                                summonerPerformanceInfo.userInfo.profileIconId
                        ),
                        summonerPerformanceInfo.championId,
                        summonerPerformanceInfo.championId.count().intValue(),
                        getWinRate(),
                        summonerPerformanceInfo.kda.avg(),
                        summonerPerformanceInfo.kills.sum(),
                        summonerPerformanceInfo.deaths.sum(),
                        summonerPerformanceInfo.assists.sum(),
                        summonerPerformanceInfo.totalMinionKills.avg(),
                        summonerPerformanceInfo.multiKills.pentaKills.sum(),
                        summonerPerformanceInfo.multiKills.quadraKiils.sum(),
                        summonerPerformanceInfo.multiKills.tripleKills.sum(),
                        summonerPerformanceInfo.championWins.eq(true).count().intValue(),
                        summonerPerformanceInfo.championWins.eq(false).count().intValue()
                )).from(summonerPerformanceInfo)
                .where(summonerPerformanceInfo.userInfo.id.eq(userId))
                .groupBy(summonerPerformanceInfo.championId)
                .orderBy(getWinRate().desc())
                .fetch();

    }

    private static NumberExpression<Double> getWinRate() {
        return summonerPerformanceInfo.championWins.eq(true).count().doubleValue()
                .divide(summonerPerformanceInfo.championId.count()).doubleValue();
    }
}
