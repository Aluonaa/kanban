package com.furiosaming.kanban.service.converter;

import com.furiosaming.kanban.persistence.model.TaskList;
import com.furiosaming.kanban.service.dto.TaskListDto;

import java.util.List;
import java.util.stream.Collectors;

public class TaskListMapper {

    public static TaskListDto taskListToDtoMap(TaskList taskList) {
        TaskListDto taskListDto = new TaskListDto();
        taskListDto.setId(taskList.getId());
        taskListDto.setName(taskList.getName());
        taskListDto.setAuthor(MemberMapper.memberToDtoMap(taskList.getAuthor()));
        taskListDto.setProject(ProjectMapper.projectToDtoMap(taskList.getProject()));
        taskListDto.setMaxTask(taskList.getMaxTask());
        return taskListDto;
    }

    public static List<TaskListDto> allTaskListDtoMap(List<TaskList> allTaskList) {
        return allTaskList.stream()
                .map(TaskListMapper::taskListToDtoMap)
                .collect(Collectors.toList()
                );
    }

    public static TaskList dtoToTaskListMap(TaskListDto taskListDto) {
        TaskList taskList = new TaskList();
        taskList.setId(taskListDto.getId());
        taskList.setName(taskListDto.getName());
        taskList.setMaxTask(taskListDto.getMaxTask());
        /**
         * автора и проект добавляем отдельно
         * **/
        return taskList;
    }
}
