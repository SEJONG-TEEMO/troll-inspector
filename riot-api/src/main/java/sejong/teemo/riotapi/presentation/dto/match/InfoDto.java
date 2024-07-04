package sejong.teemo.riotapi.presentation.dto.match;

import java.util.List;

public record InfoDto(List<TeamDto> teams, List<ParticipantDto> participants) {
}
