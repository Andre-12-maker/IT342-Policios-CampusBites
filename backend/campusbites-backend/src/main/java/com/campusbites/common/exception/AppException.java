package com.campusbites.common.exception;

public class AppException extends RuntimeException {
    private final String code;
    private final int status;

    public AppException(String code, String message, int status) {
        super(message);
        this.code   = code;
        this.status = status;
    }

    public String getCode()   { return code;   }
    public int    getStatus() { return status; }

    public static AppException notFound(String message)    { return new AppException("NOT_FOUND",    message, 404); }
    public static AppException badRequest(String message)  { return new AppException("BAD_REQUEST",  message, 400); }
    public static AppException unauthorized(String message){ return new AppException("UNAUTHORIZED", message, 401); }
    public static AppException forbidden(String message)   { return new AppException("FORBIDDEN",    message, 403); }
    public static AppException conflict(String message)    { return new AppException("CONFLICT",     message, 409); }
}