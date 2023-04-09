package com.furiosaming.kanban.service.dto.filter;

import com.furiosaming.kanban.persistence.model.enums.SortFieldTaskList;
import com.furiosaming.kanban.persistence.model.enums.SortType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "TaskListFilter", description = "Фильтр поиска списков задач")
@Data
@NoArgsConstructor
public class TaskListFilter {
    @Schema(name = "name", description = "Фильтр по названию списка задач")
    private String name;
    @Schema(name = "maxTask", description = "Фильтр по максимальному количеству задач")
    private int maxTask;
    @Schema(name = "sortFieldTaskList", description = "Поле, по которому проводится сортировка")
    private SortFieldTaskList sortFieldTaskList;
    @Schema(name = "sortType", description = "Тип сортировки (возрастающая, убывающая)")
    private SortType sortType;
}
