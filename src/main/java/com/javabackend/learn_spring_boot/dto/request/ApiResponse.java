package com.javabackend.learn_spring_boot.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;

//khi ma chuyen du lieu sang kieu json thi nhung du lieu nao null se khong chuyen vo json
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse <T>{

    //vi du mac dinh ma thanh cong la 100
    private int code = 100;
    private String message;
    private T result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
