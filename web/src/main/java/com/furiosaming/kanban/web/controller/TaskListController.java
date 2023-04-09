package com.furiosaming.kanban.web.controller;


import com.furiosaming.kanban.service.dto.TaskListDto;
import com.furiosaming.kanban.service.dto.filter.ProjectFilter;
import com.furiosaming.kanban.service.dto.filter.TaskListFilter;
import com.furiosaming.kanban.service.responseRequest.page.PagingRequestDto;
import com.furiosaming.kanban.service.responseRequest.page.PagingResponseDto;
import com.furiosaming.kanban.service.service.TaskListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/rest/taskLists")
@Tag(name = "TaskListController", description = "REST интерфейс для TaskList")

public class TaskListController {

    private final TaskListService taskListService;

    public TaskListController(TaskListService taskListService) {
        this.taskListService = taskListService;
    }

    @Operation(summary = "Получить все списки задач")
    @PostMapping(value = "/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PagingResponseDto<List<TaskListDto>> allTaskList(@Valid @RequestBody PagingRequestDto<TaskListFilter> pagingRequestDto) {
        log.debug("GET ALL TASKLISTS");
        return taskListService.getAllTaskLists(pagingRequestDto);
    }

    @Operation(summary = "Получить все списки задач проекта по id")
    @PostMapping(value = "/{id}/lists", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PagingResponseDto<List<TaskListDto>> allTaskListOfProject(@PathVariable Long id, @RequestBody PagingRequestDto<ProjectFilter> pagingRequestDto) {
        log.debug("GET ALL TASKLISTS FROM PROJECT");
        return taskListService.allTaskListOfProject(id, pagingRequestDto);
    }

    @Operation(summary = "Получить список задач по id")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseDataResponse<TaskListDto> getByIdTaskList(@PathVariable Long id) {
        log.debug("GET TASKLIST BY ID");
        return taskListService.getByIdTaskList(id);
    }

    @Operation(summary = "Создать список задач")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseDataResponse<TaskListDto> newTaskList(@RequestBody TaskListDto taskListDto) {
        log.debug("NEW/UPDATE TASKLIST");
        return taskListService.saveTaskList(taskListDto);
    }

    @Operation(summary = "Удалить список задач")
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseDataResponse<TaskListDto> deleteTaskList(@PathVariable Long id) {
        log.debug("DELETE TASKLIST BY ID");
        return taskListService.deleteTaskList(id);
    }
}

