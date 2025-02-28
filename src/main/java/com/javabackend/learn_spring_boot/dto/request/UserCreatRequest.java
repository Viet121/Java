package com.javabackend.learn_spring_boot.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;

//Annotation Data bao gom cac annotation khac nhu setter, getter, requiredArgs...(ko cho phep tao class null), ...
@Data

@Builder
/*
Binh thuong khi chung ta tao 1 class moi, va set du lieu nao do cho field cua class thi
    UserCreatRequest request = new UserCreatRequest();
    request.setUserName("Quoc Viet"); ...
Dung Lombok Builder
    UserCreatRequest request = UserCreatRequest.builder()
        .userName("Quoc Viet")
        .f...
        .build();
*/

// nguoc lai voi requiredArgs... va AllArgs..
@NoArgsConstructor

@AllArgsConstructor
// thay vi private cho cac field o duoi thi dung
@FieldDefaults(level = AccessLevel.PRIVATE) //mac dinh cho cac field khong dinh nghia la private
public class UserCreatRequest {

    // private String userName; (field dinh nghia luc ban dau la nhu nay)
    // sau khi dung Lombok annotation @FieldDefaults
    String userName;

    @Size(min = 8, message = "PASSWORD_INVALID")
    String password;
    String name;
    LocalDate dob;
    List<String> roles;
}
