package sejong.teemo.ingamesearch.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Spectator(String gameType, List<CurrentGameParticipant> participants) {
}
