package com.furiosaming.kanban.service.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.furiosaming.kanban.persistence.model.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Schema(name = "TaskDto", description = "Dto задач")
@Data
@NoArgsConstructor
public class TaskDto {
    @Schema(name = "id", description = "Идентификатор данных")
    private Long id;
    @Schema(name = "name", description = "Имя задачи")
    private String name;
    @Schema(name = "description", description = "Описание задачи")
    private String description;
    @Schema(name = "deadline", description = "Deadline")
    @JsonFormat(
            pattern = DATE_TIME,
            timezone = "UTC"
    )
    private Date deadline;
    @Schema(name = "create", description = "Дата создания")
    @JsonFormat(
            pattern = DATE_TIME,
            timezone = "UTC"
    )
    private Date create;
    @Schema(name = "update", description = "Дата последнего обновления")
    @JsonFormat(
            pattern = DATE_TIME,
            timezone = "UTC"
    )
    private Date update;
    @Schema(name = "taskListDto", description = "Список задач")
    private TaskListDto taskListDto;
    @Schema(name = "author", description = "Автор")
    private MemberDto author;
    @Schema(name = "executor", description = "Исполнитель")
    private MemberDto executor;
    @Schema(name = "status", description = "Статус")
    private Status status;
}
