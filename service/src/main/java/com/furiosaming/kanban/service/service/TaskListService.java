package com.furiosaming.kanban.service.service;

import com.furiosaming.kanban.persistence.model.Project;
import com.furiosaming.kanban.persistence.model.TaskList;
import com.furiosaming.kanban.service.dto.TaskListDto;
import com.furiosaming.kanban.service.dto.filter.ProjectFilter;
import com.furiosaming.kanban.service.dto.filter.TaskListFilter;
import com.furiosaming.kanban.service.responseRequest.base.BaseResponseDto;
import com.furiosaming.kanban.service.responseRequest.page.PagingRequestDto;
import com.furiosaming.kanban.service.responseRequest.page.PagingResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskListService {

    PagingResponseDto<List<TaskListDto>> getAllTaskLists(PagingRequestDto<TaskListFilter> pagingRequestDto);

    PagingResponseDto<List<TaskListDto>> allTaskListOfProject(Long id, PagingRequestDto<TaskListFilter> pagingRequestDto);

    BaseResponseDto<TaskListDto> getByIdTaskList(Long id);

    BaseResponseDto<TaskListDto> saveTaskList(TaskListDto taskListDto);

    BaseResponseDto<TaskListDto> deleteTaskList(Long id);

    TaskList getTaskList(Long id);

    Page<TaskList> getTaskListsByProject(Project project, Pageable pageable);
}
