package com.furiosaming.kanban.web.controller;


import com.furiosaming.kanban.service.dto.ProjectDto;
import com.furiosaming.kanban.service.dto.filter.ProjectFilter;
import com.furiosaming.kanban.service.responseRequest.base.BaseResponseDto;
import com.furiosaming.kanban.service.responseRequest.page.PagingRequestDto;
import com.furiosaming.kanban.service.responseRequest.page.PagingResponseDto;
import com.furiosaming.kanban.service.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@Slf4j
@RestController
@RequestMapping(value = "/rest/projects")
@Tag(name = "ProjectController", description = "REST интерфейс для Project")
@Valid
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Operation(summary = "Получить все проекты")
    @PostMapping(value = "/search2", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PagingResponseDto<List<ProjectDto>> allProject(@Valid @RequestBody PagingRequestDto<ProjectFilter> pagingRequestDto) {
        log.debug("GET ALL PROJECTS");
        return projectService.getAllProjects(pagingRequestDto);
    }

    @Operation(summary = "Получить проект по id")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponseDto<ProjectDto> getByIdProject(@PathVariable Long id) {
        log.debug("GET PROJECT BY ID");
        return projectService.getByIdProject(id);
    }

    @Operation(summary = "Создать проект")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponseDto<ProjectDto> newProject(@RequestBody ProjectDto projectDto) {
        log.debug("NEW/UPDATE PROJECT");
        return projectService.saveProject(projectDto);
    }

    @Operation(summary = "Удалить проект")
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponseDto<ProjectDto> deleteProject(@PathVariable Long id) {
        log.debug("DELETE PROJECT");
        return projectService.deleteProject(id);
    }
}
