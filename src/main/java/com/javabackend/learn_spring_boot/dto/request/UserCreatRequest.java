package com.javabackend.learn_spring_boot.dto.request;

import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

public class UserCreatRequest {
    private String userName;

    @Size(min = 8, message = "PASSWORD_INVALID")
    private String password;
    private String name;
    private LocalDate dob;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }
}
