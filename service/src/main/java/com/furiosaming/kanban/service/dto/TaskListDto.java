package com.furiosaming.kanban.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;


@Schema(name = "TaskListDto", description = "Dto списка задач")
@Data
@NoArgsConstructor
public class TaskListDto {
    @Schema(name = "id", description = "Идентификатор данных")
    private Long id;
    @Schema(name = "name", description = "Имя списка задач")
    private String name;
    @Schema(name = "author", description = "Автор проекта")
    private MemberDto author;
    @Schema(name = "project", description = "Проект, в котором находится список")
    private ProjectDto project;
    @Schema(name = "maxTask", description = "Максимальное количество задач")
    private int maxTask;
}

