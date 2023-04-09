package com.furiosaming.kanban.service.converter;


import com.furiosaming.kanban.persistence.model.Task;
import com.furiosaming.kanban.service.dto.TaskDto;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class TaskMapper {

    public static TaskDto taskToDtoMap(Task task) {
        TaskDto taskDto = new TaskDto();
        taskDto.setId(task.getId());
        taskDto.setName(task.getName());
        taskDto.setDescription(task.getDescription());
        taskDto.setDeadline(task.getDeadline());
        taskDto.setCreate(task.getDateOfCreate());
        taskDto.setUpdate(task.getDateOfUpdate());
        taskDto.setExecutor(MemberMapper.memberToDtoMap(task.getExecutor()));
        taskDto.setAuthor(MemberMapper.memberToDtoMap(task.getAuthor()));
        taskDto.setTaskListDto(TaskListMapper.taskListToDtoMap(task.getTaskList()));
        taskDto.setStatus(task.getStatus());
        return taskDto;
    }

    public static Task dtoToTaskMap(TaskDto taskDto) {
        Task task = new Task();
        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setDeadline(taskDto.getDeadline());
        task.setDateOfUpdate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
        /** Даты создания и обновления, участников и список задач добавляем отдельно **/
        return task;
    }

    public static List<TaskDto> allTaskDtoMap(List<Task> allTask) {
        return allTask.stream()
                .map(TaskMapper::taskToDtoMap)
                .collect(Collectors.toList());
    }
}
