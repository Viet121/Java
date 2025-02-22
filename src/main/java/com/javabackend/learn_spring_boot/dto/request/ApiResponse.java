package com.javabackend.learn_spring_boot.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
//khi ma chuyen du lieu sang kieu json thi nhung du lieu nao null se khong chuyen vo json
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse <T>{
    @Builder.Default
    //vi du mac dinh ma thanh cong la 100
    int code = 100;
    String message;
    T result;
}
