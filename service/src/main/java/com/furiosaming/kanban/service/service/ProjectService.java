package com.furiosaming.kanban.service.service;

import com.furiosaming.kanban.persistence.model.Project;
import com.furiosaming.kanban.service.dto.ProjectDto;
import com.furiosaming.kanban.service.dto.filter.ProjectFilter;
import com.furiosaming.kanban.service.responseRequest.page.PagingRequestDto;
import com.furiosaming.kanban.service.responseRequest.page.PagingResponseDto;

import java.util.List;

public interface ProjectService {

    PagingResponseDto<List<ProjectDto>> getAllProjects(PagingRequestDto<ProjectFilter> pagingRequestDto);

    BaseDataResponse<ProjectDto> getByIdProject(Long id);


    BaseDataResponse<ProjectDto> saveProject(ProjectDto projectDto);

    BaseDataResponse<ProjectDto> deleteProject(Long id);

    Project getProject(Long id);

}

