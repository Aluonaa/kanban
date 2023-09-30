package com.furiosaming.kanban.service.service;

import com.furiosaming.kanban.persistence.model.Task;
import com.furiosaming.kanban.persistence.model.TaskList;
import com.furiosaming.kanban.service.dto.TaskDto;
import com.furiosaming.kanban.service.dto.filter.TaskFilter;
import com.furiosaming.kanban.service.dto.filter.TaskListFilter;
import com.furiosaming.kanban.service.responseRequest.base.BaseResponseDto;
import com.furiosaming.kanban.service.responseRequest.page.PagingRequestDto;
import com.furiosaming.kanban.service.responseRequest.page.PagingResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {

    void updateDeadlineStatus();

    PagingResponseDto<List<TaskDto>> getAllTasks(PagingRequestDto<TaskFilter> pagingRequestDto);

    PagingResponseDto<List<TaskDto>> allTaskOfTaskList(Long id, PagingRequestDto<TaskFilter> pagingRequestDto);

    BaseResponseDto<TaskDto> getByIdTask(Long id);

    BaseResponseDto<TaskDto> saveTask(TaskDto taskDto);

    BaseResponseDto<TaskDto> deleteTask(Long id);

    Page<Task> getAllTasksByTaskList(TaskList taskList, Pageable pageable);

}

