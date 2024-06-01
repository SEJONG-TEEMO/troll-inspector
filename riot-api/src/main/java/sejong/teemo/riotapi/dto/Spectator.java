package sejong.teemo.riotapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Spectator(String gameType, List<CurrentGameParticipant> participants) {
}
