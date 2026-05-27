package com.campusbites.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Uniform API response envelope used by every controller.
 *
 * Success:  { "success": true,  "data": { ... } }
 * Failure:  { "success": false, "error": { "code": "NOT_FOUND", "message": "..." } }
 *
 * NON_NULL suppresses "data": null on errors and "error": null on successes.
 */
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