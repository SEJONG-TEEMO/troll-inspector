package sejong.teemo.riotapi.domain.match.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TeamDto(int teamId, boolean win) {
}
