package com.furiosaming.kanban.service.responseRequest.base;

import com.furiosaming.kanban.service.constants.AppConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Response", description = "Ответ пользователю")
public class BaseResponseDto<T> {

    @Schema(name = "status", description = "Статус ответа (успешно, либо одна из ошибок)")
    private boolean status;
    @Schema(name = "description", description = "Описание ошибки, либо просто успешно")
    private String description;
    @Schema(name = "result", description = "Полученные сущности в процессе обработки")
    private T result;

    public static class Builder<T> {

        private BaseResponseDto<T> baseResponseDto;
        public Builder() {
            baseResponseDto = new BaseResponseDto<>();
        }

        public BaseResponseDto.Builder<T> success(T result) {
            baseResponseDto.status = true;
            baseResponseDto.description = AppConstants.success;
            baseResponseDto.result = result;
            return this;
        }

        public BaseResponseDto.Builder<T> notFound(String description) {
            baseResponseDto.status = false;
            baseResponseDto.description = description;
            baseResponseDto.result = null;
            return this;
        }

        public BaseResponseDto.Builder<T> missing(String description) {
            baseResponseDto.status = false;
            baseResponseDto.description = description;
            baseResponseDto.result = null;
            return this;
        }

        public BaseResponseDto.Builder<T> alreadyExist() {
            baseResponseDto.status = false;
            baseResponseDto.description = AppConstants.alreadyExists;
            baseResponseDto.result = null;
            return this;
        }

        public BaseResponseDto<T> build() {
            return baseResponseDto;
        }
    }
}
