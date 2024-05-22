package sejong.teemo.trollinspector.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.*;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation.Builder.ContainerBuilder;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sejong.teemo.trollinspector.record.*;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import static co.elastic.clients.elasticsearch._types.aggregations.Aggregation.Builder;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static sejong.teemo.trollinspector.util.parsing.JsonToPlayerPerformance.parseKdaScore;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlayerStatsService {

    private final ElasticsearchClient elasticsearchClient;

    public List<SummonerPerformanceRecord> searchGameData(String username) {

        try {
            SearchResponse<SummonerPerformanceRecord> searchResponse = elasticsearchClient.search(s -> s
                            .index("player_performance")
                            .query(q -> q
                                    .match(t -> t
                                            .field("username")
                                            .query(username)
                                    )
                            ),
                    SummonerPerformanceRecord.class
            );

            return searchResponse.hits().hits().stream()
                    .map(Hit::source)
                    .collect(toList());
        } catch (Exception e) {
            log.error("Elasticsearch rest client error", e);
            throw new IllegalArgumentException();
        }
    }

    public GameInspectorRecord analyzePerformance(String username) throws IOException {

        String jsonQuery = new String(Files.readAllBytes(Paths.get("troll-inspector/src/main/resources/aggregation_query.json")));

        // 카테고리 분류 수행
        Query query = MatchQuery.of(m -> m
                .field("username")
                .query(username)
        )._toQuery();

        // Perform aggregation analysis
        SearchResponse<AggregationResultsRecord> search = elasticsearchClient.search(s -> s
                        .index("player_performance")
                        .size(0) // Don't return any documents, only the aggregation results
                        .query(query)
                        .aggregations("performance_stats", this::buildPerformanceStatsAggregation)
                        .withJson(new StringReader(jsonQuery))
                        .ignoreUnavailable(true),
                AggregationResultsRecord.class
        );


        AggregationResultsRecord aggregationResultsRecord = aggregateSummonerResults(search);
        List<PositionResultRecord> positionResultRecords = aggregatePositionResults(search);

        double sum = positionResultRecords.stream()
                .mapToDouble(PositionResultRecord::finalScore)
                .sum();

        return GameInspectorRecord.of(aggregationResultsRecord, positionResultRecords, sum);
    }

    /**
     * 소환사 및 소환사가 과거 플레이한 챔피언별 지표 집계
     *
     * @param builder
     * @return
     */
    ContainerBuilder buildPerformanceStatsAggregation(Builder builder) {
        return builder.terms(t -> t
                .field("username")
                .size(20)
        ).aggregations("average_kills", avg -> addAverageAggregation(avg, "kills")
        ).aggregations("average_deaths", avg -> addAverageAggregation(avg, "deaths")
        ).aggregations("average_assists", avg -> addAverageAggregation(avg, "assists")
        ).aggregations("average_kda", avg -> addAverageAggregation(avg, "kda")
        ).aggregations("champion_frequency", champion -> champion
                .terms(t -> t
                        .field("championId")
                        .size(20)
                ).aggregations("average_kills", avg -> addAverageAggregation(avg, "kills")
                ).aggregations("average_deaths", avg -> addAverageAggregation(avg, "deaths")
                ).aggregations("average_assists", avg -> addAverageAggregation(avg, "assists")
                ).aggregations("average_kda", avg -> addAverageAggregation(avg, "kda")
                )
        );
    }

    AggregationResultsRecord aggregateSummonerResults(SearchResponse<AggregationResultsRecord> search) {
        return search.aggregations().get("performance_stats").sterms().buckets().array().stream()
                .map(this::mapToAggregationResultsRecord)
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    AggregationResultsRecord mapToAggregationResultsRecord(StringTermsBucket bucket) {
        Map<String, Aggregate> aggregations = bucket.aggregations();
        Double averageKills = getAverage(aggregations, "average_kills");
        Double averageDeaths = getAverage(aggregations, "average_deaths");
        Double averageAssists = getAverage(aggregations, "average_assists");
        Double averageKda = getAverage(aggregations, "average_kda");

        Map<Long, SummonerStatsRecord> summonerStatsRecordMap = aggregations.get("champion_frequency").lterms().buckets().array().stream()
                .collect(Collectors.toMap(
                        LongTermsBucket::key,
                        championBucket -> mapToSummonerStatsRecord(championBucket, bucket.key().stringValue())
                ));

        return AggregationResultsRecord.of(
                SummonerStatsRecord.of(bucket.key().stringValue(), averageKills, averageDeaths, averageAssists, averageKda, bucket.docCount()),
                summonerStatsRecordMap
        );
    }

    List<PositionResultRecord> aggregatePositionResults(SearchResponse<AggregationResultsRecord> search) {
        return search.aggregations().get("position").sterms().buckets().array().stream()
                .map(this::mapToPositionResultRecord)
                .toList();
    }

    private ContainerBuilder addAverageAggregation(Builder builder, String field) {
        return builder.avg(avgAgg -> avgAgg.field(field));
    }


    Double getAverage(Map<String, Aggregate> aggregations, String key) {
        return ofNullable(aggregations.get(key))
                .map(Aggregate::avg)
                .map(AvgAggregate::value)
                .orElse(0.0);
    }

    PositionResultRecord mapToPositionResultRecord(StringTermsBucket bucket) {
        Map<String, Aggregate> aggregations = bucket.aggregations();
        String lane = bucket.key().stringValue();

        return getKdaScoreAggregate(aggregations)
                .map(jsonData -> parseKdaScore(jsonData, lane))
                .orElseThrow(NoSuchElementException::new);
    }

    private Optional<JsonData> getKdaScoreAggregate(Map<String, Aggregate> aggregations) {
        return ofNullable(aggregations.get("KDA_score"))
                .map(Aggregate::scriptedMetric)
                .map(ScriptedMetricAggregate::value);
    }

    SummonerStatsRecord mapToSummonerStatsRecord(LongTermsBucket bucket, String username) {
        Map<String, Aggregate> aggregations = bucket.aggregations();
        Double averageKills = getAverage(aggregations, "average_kills");
        Double averageDeaths = getAverage(aggregations, "average_deaths");
        Double averageAssists = getAverage(aggregations, "average_assists");
        Double averageKda = getAverage(aggregations, "average_kda");

        return SummonerStatsRecord.of(username, averageKills, averageDeaths, averageAssists, averageKda, bucket.docCount());
    }
}