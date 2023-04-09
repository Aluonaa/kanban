package com.furiosaming.kanban.service.dto.filter;

import com.furiosaming.kanban.persistence.model.enums.SortFieldProject;
import com.furiosaming.kanban.persistence.model.enums.SortType;
import com.furiosaming.kanban.service.dto.MemberDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "ProjectFilter", description = "Фильтр поиска проектов")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectFilter {
    @Schema(name = "name", description = "Название проекта")
    private String name;
    @Schema(name = "author", description = "Создатель проекта")
    private MemberDto author;
    @Schema(name = "sortFieldProject", description = "Поле, по которому проводится сортировка")
    private SortFieldProject sortFieldProject;
    @Schema(name = "sortType", description = "Тип сортировки (возрастающая, убывающая)")
    private SortType sortType;
}

