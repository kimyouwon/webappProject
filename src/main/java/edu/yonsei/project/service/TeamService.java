package edu.yonsei.project.service;

import edu.yonsei.project.dto.TeamDto;
import edu.yonsei.project.entity.TeamEntity;
import edu.yonsei.project.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static java.lang.Character.getName;

@Service
@RequiredArgsConstructor

public class TeamService {
    private final TeamRepository teamRepository;

    public TeamEntity postTeam(
            TeamDto teamDto
    ){
        var entity = TeamEntity.builder()
                .name(teamDto.getName())
                .pm(teamDto.getPm())
                .member1(teamDto.getMember1())
                .member2(teamDto.getMember2())
                .member3(teamDto.getMember3())
                .build()
                ;
        return teamRepository.save(entity);
    }
}
