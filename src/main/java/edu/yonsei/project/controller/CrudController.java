package edu.yonsei.project.controller;

import edu.yonsei.project.dto.TeamDto;
import edu.yonsei.project.entity.TeamEntity;
import edu.yonsei.project.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
//테스트
@RestController
@RequiredArgsConstructor
@RequestMapping("yonsei")

public class CrudController {
    private final TeamService teamService;

    @PostMapping(path="/team/{name}")

    public TeamEntity func1(

            @PathVariable String name,
            @RequestBody TeamDto teamDto
    ){
        return teamService.postTeam(teamDto);
    }
}
