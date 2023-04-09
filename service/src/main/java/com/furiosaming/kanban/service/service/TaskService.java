package com.furiosaming.kanban.service.service;

import com.furiosaming.kanban.persistence.model.Task;
import com.furiosaming.kanban.persistence.model.TaskList;
import com.furiosaming.kanban.service.dto.TaskDto;
import com.furiosaming.kanban.service.dto.filter.TaskFilter;
import com.furiosaming.kanban.service.dto.filter.TaskListFilter;
import com.furiosaming.kanban.service.responseRequest.page.PagingRequestDto;
import com.furiosaming.kanban.service.responseRequest.page.PagingResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {

    void updateDeadlineStatus();

    PagingResponseDto<List<TaskDto>> getAllTasks(PagingRequestDto<TaskFilter> pagingRequestDto);

    PagingResponseDto<List<TaskDto>> allTaskOfTaskList(Long id, PagingRequestDto<TaskListFilter> pagingRequestDto);

    BaseDataResponse<TaskDto> getByIdTask(Long id);

    BaseDataResponse<TaskDto> saveTask(TaskDto taskDto);

    BaseDataResponse<TaskDto> deleteTask(Long id);

    Page<Task> getAllTasksByTaskList(TaskList taskList, Pageable pageable);

}

