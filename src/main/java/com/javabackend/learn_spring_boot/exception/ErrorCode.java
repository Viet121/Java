package com.javabackend.learn_spring_boot.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    //loi khong xac dinh
    UNCATEGORIZED_EXCEPTION(999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    //user da ton tai
    USER_EXISTED(101, "User existed", HttpStatus.NOT_FOUND),
    //tu khoa khong ton tai
    INVALID_KEY(102, "Uncategorized error", HttpStatus.BAD_REQUEST),
    //user khong ton tai
    USER_NOT_FOUND(103,"User not found", HttpStatus.NOT_FOUND),
    //pass khong dung hoac khong xac nhan duoc user
    UNAUTHENTICATED(104,"Unauthenticated", HttpStatus.UNAUTHORIZED),
    //khong co quyen thuc hien request (permission su cho phep)
    UNAUTHORIZED(105,"You do not have permission", HttpStatus.FORBIDDEN),
    //pass duoc tao khong hop le
    PASSWORD_INVALID(106,"Password must be at least 8 character",HttpStatus.BAD_REQUEST),
    //khong du tuoi
    INVALID_DOB(107,"Invalid date of birth", HttpStatus.BAD_REQUEST)
    ;

    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    private int code;
    private String message;
    private HttpStatus httpStatus;

}
