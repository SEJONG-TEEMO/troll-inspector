package sejong.teemo.riotapi.domain.match.dto;

import java.util.List;

public record InfoDto(List<TeamDto> teams, List<ParticipantDto> participants) {
}
