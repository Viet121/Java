package com.javabackend.learn_spring_boot.mapper;

import com.javabackend.learn_spring_boot.dto.request.UserCreatRequest;
import com.javabackend.learn_spring_boot.dto.request.UserUpdateRequest;
import com.javabackend.learn_spring_boot.dto.response.UserResponse;
import com.javabackend.learn_spring_boot.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    // Method nay tu dong tao moi mot User va tra ve mot User voi field tu UserCreatRequest
    User toUser(UserCreatRequest request);

    UserResponse toUserResponse(User user);

    // Method nay khong tao va tra ve User, chi tu set() du lieu tu UserUpdateRequest cho mot User da tao truoc do (tham tri)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);

}
