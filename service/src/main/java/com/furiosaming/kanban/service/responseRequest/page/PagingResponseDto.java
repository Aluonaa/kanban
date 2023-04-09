package com.furiosaming.kanban.service.responseRequest.page;


import com.furiosaming.kanban.service.constants.AppConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Response", description = "Ответ пользователю")
public class PagingResponseDto<T> {

    @Schema(name = "status", description = "Статус ответа (успешно, либо одна из ошибок)")
    private boolean status;
    @Schema(name = "description", description = "Описание ошибки, либо просто успешно")
    private String description;
    @Schema(name = "result", description = "Полученные сущности в процессе обработки")
    private T result;
    @Schema(name = "page", description = "Номер страницы для вывода")
    private int page;
    @Schema(name = "limit", description = "Количество записей на странице")
    private int limit;
    @Schema(name = "total", description = "Общее количество записей")
    private long total;

    public static class Builder<T> {
        private PagingResponseDto<T> pagingResponseDto;

        public Builder() {
            pagingResponseDto = new PagingResponseDto<>();
        }

        public Builder<T> success(T result, int page, int limit, long total) {
            pagingResponseDto.page = page;
            pagingResponseDto.limit = limit;
            pagingResponseDto.total = total;
            pagingResponseDto.status = true;
            pagingResponseDto.description = AppConstants.success;
            pagingResponseDto.result = result;
            return this;
        }

        public Builder<T> success(T result) {
            pagingResponseDto.status = true;
            pagingResponseDto.description = AppConstants.success;
            pagingResponseDto.result = result;
            return this;
        }

        public Builder<T> notFound() {
            pagingResponseDto.status = false;
            pagingResponseDto.description = AppConstants.notFound;
            pagingResponseDto.result = null;
            return this;
        }

        public Builder<T> missing() {
            pagingResponseDto.status = false;
            pagingResponseDto.description = AppConstants.missing;
            pagingResponseDto.result = null;
            return this;
        }

        public Builder<T> alreadyExist() {
            pagingResponseDto.status = false;
            pagingResponseDto.description = AppConstants.alreadyExists;
            pagingResponseDto.result = null;
            return this;
        }

        public PagingResponseDto<T> build() {
            return pagingResponseDto;
        }
    }

}
