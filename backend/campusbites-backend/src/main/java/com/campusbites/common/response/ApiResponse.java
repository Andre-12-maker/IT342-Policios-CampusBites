package com.campusbites.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(boolean success, T data, ErrorPayload error) {

    public record ErrorPayload(String code, String message) {}

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, data, null);
    }

    public static <T> ApiResponse<T> fail(String code, String message) {
        return new ApiResponse<>(false, null, new ErrorPayload(code, message));
    }
}