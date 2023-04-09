package com.furiosaming.kanban.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Schema(name = "MemberDto", description = "Dto участника")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"upn"})
public class MemberDto {
    @Schema(name = "upn", description = "User Principal Name ")
    private String upn;
    @Schema(description = "Имя сотрудника")
    private String firstName;
    @Schema(description = "Фамилия сотрудника")
    private String lastName;
    @Schema(description = "Отчество сотрудника")
    private String middleName;

}
