package sejong.teemo.riotapi.dto;

public record Champion(
        int kills,
        int deaths,
        int assists,
        double rating,
        int recentGameCount
) {
}
