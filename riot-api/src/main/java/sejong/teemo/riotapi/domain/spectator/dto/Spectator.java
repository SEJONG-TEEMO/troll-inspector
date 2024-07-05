package sejong.teemo.riotapi.domain.spectator.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Spectator(Long gameId, String gameType, List<CurrentGameParticipant> participants) {
}
