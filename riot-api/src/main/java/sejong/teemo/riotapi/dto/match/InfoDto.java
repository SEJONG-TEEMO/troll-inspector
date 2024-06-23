package sejong.teemo.riotapi.dto.match;

import java.util.List;

public record InfoDto(List<TeamDto> teams, List<ParticipantDto> participants) {
}
