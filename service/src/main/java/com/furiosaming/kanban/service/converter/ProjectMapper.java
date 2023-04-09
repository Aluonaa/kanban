package com.furiosaming.kanban.service.converter;

import com.furiosaming.kanban.persistence.model.Project;
import com.furiosaming.kanban.service.dto.ProjectDto;

import java.util.List;
import java.util.stream.Collectors;

public class ProjectMapper {
    public static ProjectDto projectToDtoMap(Project project) {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setId(project.getId());
        projectDto.setName(project.getName());
        projectDto.setAuthor(MemberMapper.memberToDtoMap(project.getAuthor()));
        return projectDto;
    }

    public static Project dtoToProjectMap(ProjectDto projectDto) {
        Project project = new Project();
        project.setId(projectDto.getId());
        project.setName(projectDto.getName());
        return project;
    }

    public static List<ProjectDto> allProjectDtoMap(List<Project> allProject) {
        return allProject.stream()
                .map(ProjectMapper::projectToDtoMap)
                .collect(Collectors.toList()
                );
    }
}
