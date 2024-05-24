package edu.yonsei.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
