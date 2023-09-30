package com.furiosaming.kanban.service.dto.filter;

import com.furiosaming.kanban.persistence.model.enums.SortFieldTask;
import com.furiosaming.kanban.persistence.model.enums.SortType;
import com.furiosaming.kanban.persistence.model.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "TaskFilter", description = "Фильтр поиска задач")
@Data
@NoArgsConstructor
public class TaskFilter {
    @Schema(name = "name", description = "Фильтр по имени задачи")
    private String name;
    @Schema(name = "author", description = "Фильтр по автору")
    private String author;
    @Schema(name = "status", description = "Статус")
    private Status status;
    @Schema(name = "sortFieldTask", description = "Поле, по которому проводится сортировка")
    private SortFieldTask sortFieldTask;
    @Schema(name = "sortType", description = "Тип сортировки(возрастающая, убывающая)")
    private SortType sortType;
}

