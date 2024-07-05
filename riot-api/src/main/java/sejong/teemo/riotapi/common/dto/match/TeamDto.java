package sejong.teemo.riotapi.common.dto.match;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TeamDto(int teamId, boolean win) {
}
