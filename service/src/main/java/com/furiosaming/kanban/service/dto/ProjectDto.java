package com.furiosaming.kanban.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "ProjectDto", description = "Dto проекта")
@Data
@NoArgsConstructor
public class ProjectDto {
    @Schema(name = "id", description = "Идентификатор данных")
    private Long id;
    @Schema(name = "name", description = "Имя проекта")
    private String name;
    @Schema(name = "author", description = "Автор проекта")
    private MemberDto author;
}
