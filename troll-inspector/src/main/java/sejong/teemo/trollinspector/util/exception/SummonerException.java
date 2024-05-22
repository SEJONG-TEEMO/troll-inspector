package sejong.teemo.trollinspector.util.exception;

public class SummonerException extends RuntimeException {

    public enum ErrorType {
        NOT_FOUND_USERNAME,
        NOT_FOUND_PUUID,
        INVALID_DATA,
        ALREADY_EXISTS,
    }

    public SummonerException(ErrorType errorType, String detail) {
        super(generateMessage(errorType, detail));
    }

    private static String generateMessage(ErrorType errorType, String detail) {
        switch (errorType) {
            case NOT_FOUND_USERNAME:
                return String.format("Summoner name with %s could not be found.", detail);
            case NOT_FOUND_PUUID:
                return String.format("PUUID with %s could not be found.", detail);
            case INVALID_DATA:
                return String.format("Invalid data for player: %s.", detail);
            case ALREADY_EXISTS:
                return String.format("Summoner with %s already exists.", detail);
            default:
                return "An error occurred with player management.";
        }
    }
}

