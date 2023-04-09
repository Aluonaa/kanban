package com.furiosaming.kanban.web.controller;


import com.furiosaming.kanban.service.dto.TaskDto;
import com.furiosaming.kanban.service.dto.filter.TaskFilter;
import com.furiosaming.kanban.service.dto.filter.TaskListFilter;
import com.furiosaming.kanban.service.responseRequest.page.PagingRequestDto;
import com.furiosaming.kanban.service.responseRequest.page.PagingResponseDto;
import com.furiosaming.kanban.service.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "rest/tasks")
@Tag(name = "TaskController", description = "REST интерфейс для Task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Получить все задачи")
    @PostMapping(value = "/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PagingResponseDto<List<TaskDto>> allTask(@RequestBody PagingRequestDto<TaskFilter> pagingRequestDto) {
        log.debug("GET ALL TASKS");
        return taskService.getAllTasks(pagingRequestDto);
    }

    @Operation(summary = "Получить все задачи списка задач по id")
    @PostMapping(value = "/{id}/tasks", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PagingResponseDto<List<TaskDto>> allTaskOfTaskList(@PathVariable Long id, @RequestBody PagingRequestDto<TaskListFilter> pagingRequestDto) {
        log.debug("GET ALL TASKS FROM TASKLIST");
        return taskService.allTaskOfTaskList(id, pagingRequestDto);
    }

    @Operation(summary = "Получить задачу по id")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseDataResponse<TaskDto> getByIdTask(@PathVariable Long id) {
        log.debug("GET TASK BY ID");
        return taskService.getByIdTask(id);
    }

    @Operation(summary = "Создать задачу")
    @PostMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseDataResponse<TaskDto> newTask(@RequestBody TaskDto taskDto) {
        log.debug("NEW/UPDATE TASK");
        return taskService.saveTask(taskDto);
    }

    @Operation(summary = "Удалить задачу")
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseDataResponse<TaskDto> deleteTask(@PathVariable Long id) {
        log.debug("DELETE TASK BY ID");
        return taskService.deleteTask(id);
    }


