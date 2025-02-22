package com.javabackend.learn_spring_boot.exception;

public enum ErrorCode {
    USER_EXISTED(101, "User existed"),
    INVALID_KEY(102, "Uncategorized error"),
    USER_NOT_FOUND(103,"User not found"),
    PASSWORD_INVALID(103,"Password must be at least 8 character"),
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error"),
    ;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
