package sejong.teemo.trollinspector.record;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

import static sejong.teemo.trollinspector.util.parsing.MathematicalParsing.roundToTwoDecimalPlaces;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GameInspectorRecord(
        AggregationResultsRecord aggregationResultsRecord,
        List<PositionResultRecord> positionResultRecord,
        Double score
) {
    public static GameInspectorRecord of(
            AggregationResultsRecord aggregationResultsRecord,
            List<PositionResultRecord> positionResultRecord,
            Double score
    ) {
        return new GameInspectorRecord(
                aggregationResultsRecord,
                positionResultRecord,
                roundToTwoDecimalPlaces(score)
        );
    }
}
