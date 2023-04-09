package com.furiosaming.kanban.service.responseRequest.page;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@Schema(name = "PagingRequest", description = "Запрос страницы")
@Valid
public class PagingRequestDto<T> {
    @Schema(name = "page", description = "Номер страницы")
    @Min(value = 0, message = "Номер страницы не может быть меньше нуля")
    private int page;
    @Min(value = 1, message = "Количество записей на странице должно быть больше нуля")
    @Schema(name = "limit", description = "Количество записей на странице")
    private int limit;
    @Schema(name = "filter", description = "Фильтр записей")
    private T filter;
}
