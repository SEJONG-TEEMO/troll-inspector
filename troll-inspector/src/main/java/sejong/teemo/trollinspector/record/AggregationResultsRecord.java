package sejong.teemo.trollinspector.record;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AggregationResultsRecord(
        SummonerStatsRecord aggregations,
        Map<Long, SummonerStatsRecord> championsData
) {
    public static AggregationResultsRecord of(
            SummonerStatsRecord aggregations,
            Map<Long, SummonerStatsRecord> championsData
    ) {
        return new AggregationResultsRecord(
                aggregations,
                championsData
        );
    }
}
