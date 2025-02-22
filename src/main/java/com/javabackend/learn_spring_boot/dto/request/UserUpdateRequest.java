package com.javabackend.learn_spring_boot.dto.request;

import java.time.LocalDate;

public class UserUpdateRequest {
    private String password;
    private String name;
    private LocalDate dob;

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
