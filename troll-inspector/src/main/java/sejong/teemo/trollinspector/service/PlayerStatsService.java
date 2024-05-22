package sejong.teemo.trollinspector.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.*;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.springframework.stereotype.Service;
import sejong.teemo.trollinspector.record.*;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlayerStatsService {


    public final static String SCHEME = "http";
    public final static String HOST_NAME = "localhost";
    public final static int PORT = 9200;
    public final static String APIKEY = "d0JEMGk0OEJGVmVuV2xqZzRhVHA6cDJLd21CdU5SaC1vU1JNM0ZIVEJ4Zw==";

    public List<SummonerPerformanceRecord> searchGameData(String searchText) {

        try {
            // Create the low-level client
            RestClient restClient = RestClient
                    .builder(new HttpHost(HOST_NAME, PORT, SCHEME))
                    .setDefaultHeaders(new Header[]{
                            new BasicHeader("Authorization", "ApiKey " + APIKEY)
                    })
                    .build();

            ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
            ElasticsearchClient esClient = new ElasticsearchClient(transport);

            SearchResponse<SummonerPerformanceRecord> searchResponse = esClient.search(s -> s
                            .index("player_performance")
                            .query(q -> q
                                    .match(t -> t
                                            .field("username")
                                            .query(searchText)
                                    )
                            ),
                    SummonerPerformanceRecord.class
            );

            return searchResponse.hits().hits().stream()
                    .map(Hit::source)
                    .collect(toList());
        } catch (Exception e) {
            log.error("Elasticsearch rest client error", e);
        }

        return null;
    }

    public GameInspectorRecord performAnalysis(String username) throws IOException {

        // Create the low-level client
        org.elasticsearch.client.RestClient restClient = org.elasticsearch.client.RestClient
                .builder(new HttpHost(HOST_NAME, PORT, SCHEME))
                .setDefaultHeaders(new Header[]{
                        new BasicHeader("Authorization", "ApiKey " + APIKEY)
                })
                .build();

        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        ElasticsearchClient esClient = new ElasticsearchClient(transport);

        // Perform category classification: MatchAllQuery로 변경하여 모든 데이터를 대상으로 집계 수행
//        Query query = MatchAllQuery.of(m -> m)._toQuery();
        String jsonQuery = new String(Files.readAllBytes(Paths.get("troll-inspector/src/main/resources/aggregation_query.json")));
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonQuery);

        // 카테고리 분류 수행
        Query query = MatchQuery.of(m -> m
                .field("username")
                .query(username)
        )._toQuery();

        // Perform aggregation analysis
        SearchResponse<AggregationResultsRecord> search = esClient.search(s -> s
                        .index("player_performance")
                        .size(0) // Don't return any documents, only the aggregation results
                        .query(query)
                        .aggregations("performance_stats", a -> a
                                        .terms(t -> t
                                                .field("username") // Aggregate by username
                                                .size(20)
                                        ) // Set to 20 for testing
                                        .aggregations("average_kills", avg -> avg
                                                .avg(avgAgg -> avgAgg
                                                        .field("kills") // Calculate average kills
                                                )
                                        )
                                        .aggregations("average_deaths", avg -> avg
                                                .avg(avgAgg -> avgAgg
                                                        .field("deaths") // Calculate average deaths
                                                )
                                        )
                                        .aggregations("average_assists", avg -> avg
                                                .avg(avgAgg -> avgAgg
                                                        .field("assists") // Calculate average assists
                                                )
                                        )
                                        .aggregations("average_kda", avg -> avg
                                                .avg(avgAgg -> avgAgg
                                                        .field("kda") // Calculate average KDA
                                                )
                                        )
                                        .aggregations("champion_frequency", champion -> champion
                                                .terms(t -> t
                                                        .field("championId")
                                                        .size(20) // 테스트를 위해 20으로 설정
                                                )
                                                .aggregations("average_kills", avg -> avg
                                                        .avg(avgAgg -> avgAgg
                                                                .field("kills") // Calculate average kills
                                                        )
                                                )
                                                .aggregations("average_deaths", avg -> avg
                                                        .avg(avgAgg -> avgAgg
                                                                .field("deaths") // Calculate average deaths
                                                        )
                                                )
                                                .aggregations("average_assists", avg -> avg
                                                        .avg(avgAgg -> avgAgg
                                                                .field("assists") // Calculate average assists
                                                        )
                                                )
                                                .aggregations("average_kda", avg -> avg
                                                        .avg(avgAgg -> avgAgg
                                                                .field("kda") // Calculate average KDA
                                                        )
                                                )
                                        )
                        )
                        .withJson(new StringReader(jsonQuery)) // JSON 기반의 집계 쿼리 추가
//                .aggregations(map)
                        .ignoreUnavailable(true)
                , AggregationResultsRecord.class
        );

        List<StringTermsBucket> performanceStats = search.aggregations().get("performance_stats").sterms().buckets().array();

        AggregationResultsRecord aggregationResultsRecord = performanceStats.stream()
                .map(bucket -> {
                    Map<String, Aggregate> aggregations = bucket.aggregations();
                    Double averageKills = requireNonNull(ofNullable(aggregations.get("average_kills")).map(Aggregate::avg)
                            .map(AvgAggregate::value)
                            .orElse(null));
                    Double averageDeaths = requireNonNull(ofNullable(aggregations.get("average_deaths")).map(Aggregate::avg)
                            .map(AvgAggregate::value)
                            .orElse(null));
                    Double averageAssists = requireNonNull(ofNullable(aggregations.get("average_assists")).map(Aggregate::avg)
                            .map(AvgAggregate::value)
                            .orElse(null));
                    Double averageKda = requireNonNull(ofNullable(aggregations.get("average_kda")).map(Aggregate::avg)
                            .map(AvgAggregate::value)
                            .orElse(null));
                    List<LongTermsBucket> champion_frequency = aggregations.get("champion_frequency").lterms().buckets().array();

                    Map<Long, SummonerStatsRecord> summonerStatsRecordMap = champion_frequency.stream()
                            .collect(Collectors.toMap(
                                    LongTermsBucket::key, // Key Mapping
                                    championBucket -> { // Value Mapping
                                        Map<String, Aggregate> championAggregations = championBucket.aggregations();
                                        Double championAverageKills = ofNullable(championAggregations.get("average_kills"))
                                                .map(Aggregate::avg)
                                                .map(AvgAggregate::value)
                                                .orElse(null);
                                        Double championAverageDeaths = ofNullable(championAggregations.get("average_deaths"))
                                                .map(Aggregate::avg)
                                                .map(AvgAggregate::value)
                                                .orElse(null);
                                        Double championAverageAssists = ofNullable(championAggregations.get("average_assists"))
                                                .map(Aggregate::avg)
                                                .map(AvgAggregate::value)
                                                .orElse(null);
                                        Double championAverageKda = ofNullable(championAggregations.get("average_kda"))
                                                .map(Aggregate::avg)
                                                .map(AvgAggregate::value)
                                                .orElse(null);

                                        return SummonerStatsRecord.of(
                                                bucket.key().stringValue(),
                                                championAverageKills,
                                                championAverageDeaths,
                                                championAverageAssists,
                                                championAverageKda,
                                                championBucket.docCount());
                                    }
                            ));
                    return AggregationResultsRecord.of(
                            SummonerStatsRecord.of(
                                    bucket.key().stringValue(),
                                    averageKills,
                                    averageDeaths,
                                    averageAssists,
                                    averageKda,
                                    bucket.docCount()),
                            summonerStatsRecordMap

                    );
                }).findFirst().orElseThrow(NoSuchElementException::new);

        List<StringTermsBucket> positionStats = search.aggregations().get("position").sterms().buckets().array();

        AtomicReference<Double> sum = new AtomicReference<>((double) 0);
        AtomicReference<Long> size = new AtomicReference<>((long) 0);

        List<PositionResultRecord> positionRecords = positionStats.stream()
                .map(bucket -> {
                    Map<String, Aggregate> aggregations = bucket.aggregations();
                    String lane = bucket.key().stringValue();

                    JsonData kdaScoreAggregate = ofNullable(aggregations.get("KDA_score"))
                            .map(Aggregate::scriptedMetric)
                            .map(ScriptedMetricAggregate::value)
                            .orElse(null);

                    if (kdaScoreAggregate != null) {
                        try {
                            ObjectMapper mapper = new ObjectMapper();
                            log.info("kda : {}", kdaScoreAggregate.toJson());

                            PositionResultRecord positionResultRecord = mapper.readValue(kdaScoreAggregate.toJson().toString(), PositionResultRecord.class);

                            log.info("record : {}", positionResultRecord);

                            sum.updateAndGet(v -> v + positionResultRecord.finalScore());
                            size.updateAndGet(v -> v + positionResultRecord.count());

                            return PositionResultRecord.of(lane, positionResultRecord.finalScore(), positionResultRecord.totalScore(), positionResultRecord.count());
                        } catch (Exception e) {
                            log.error("Error while extracting KDA Score", e);
                        }
                    } throw new NoSuchElementException();
                }).toList();

        log.info("positionRecords: {}", positionRecords);

        return GameInspectorRecord.of(aggregationResultsRecord, positionRecords, sum.get());
    }

}