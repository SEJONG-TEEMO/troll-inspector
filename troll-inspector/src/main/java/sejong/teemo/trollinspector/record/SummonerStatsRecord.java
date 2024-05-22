package sejong.teemo.trollinspector.record;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import static sejong.teemo.trollinspector.util.parsing.MathematicalParsing.roundToTwoDecimalPlaces;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SummonerStatsRecord(
        String key,
        Double averageKills,
        Double averageDeaths,
        Double averageAssists,
        Double averageKda,
        Long docCount
) {
    public static SummonerStatsRecord of(
            String key,
            Double averageKills,
            Double averageDeaths,
            Double averageAssists,
            Double averageKda,
            Long docCount
    ) {
        return new SummonerStatsRecord(
                key,
                roundToTwoDecimalPlaces(averageKills),
                roundToTwoDecimalPlaces(averageDeaths),
                roundToTwoDecimalPlaces(averageAssists),
                roundToTwoDecimalPlaces(averageKda),
                docCount
        );
    }
}
