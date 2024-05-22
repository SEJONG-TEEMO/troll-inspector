package sejong.teemo.trollinspector.util.parsing.exception;

public class SummonerException extends RuntimeException {
    // Player 관련 에러 상황을 설명하는 열거형
    public enum ErrorType {
        NOT_FOUND_USERNAME,
        NOT_FOUND_PUUID,
        INVALID_DATA,
        ALREADY_EXISTS,
        // 여기에 필요한 다른 에러 타입 추가 가능
    }

    // 에러 타입에 따른 메시지를 관리할 수 있도록 생성자 추가
    public SummonerException(ErrorType errorType, String detail) {
        super(generateMessage(errorType, detail));
    }

    // 에러 타입과 상세 정보를 바탕으로 에러 메시지 생성
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

