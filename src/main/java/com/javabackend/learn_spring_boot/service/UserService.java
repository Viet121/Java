package com.javabackend.learn_spring_boot.service;

import com.javabackend.learn_spring_boot.dto.request.UserCreatRequest;
import com.javabackend.learn_spring_boot.dto.request.UserUpdateRequest;
import com.javabackend.learn_spring_boot.dto.response.UserResponse;
import com.javabackend.learn_spring_boot.exception.AppException;
import com.javabackend.learn_spring_boot.exception.ErrorCode;
import com.javabackend.learn_spring_boot.mapper.UserMapper;
import com.javabackend.learn_spring_boot.model.User;
import com.javabackend.learn_spring_boot.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
// 2 dong duoi giup ta khoi viet annotation khi su dung cac repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;

    //danh sach user
    public List<User> getUsers() {
        return userRepository.findAll();
    }
    //1 user theo id
    public User getUserByUserId(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }
    //1 user theo userName
    public UserResponse getUserByUserName(String userName) {
        return userMapper.toUserResponse(userRepository.findByUserName(userName)
                .orElseThrow(() -> new RuntimeException("User not found")));
    }
    //them user
    public User createUser(UserCreatRequest userRequest){

        if(userRepository.existsByUserName(userRequest.getUserName()))
            throw new AppException(ErrorCode.USER_EXISTED);

//        User user = new User();
//        user.setUserName(userRequest.getUserName());
//        user.setPassword(userRequest.getPassword());
//        user.setName(userRequest.getName());
//        user.setDob(userRequest.getDob());

        User user = userMapper.toUser(userRequest); //1 dong nay dung mapper thay cho 5 dong tren
        return userRepository.save(user);
    }

    // cap nhat user
    public User updateUser(String userId, UserUpdateRequest request) {
        // tim va tao User tu userId
        User user = getUserByUserId(userId);

//        user.setPassword(request.getPassword());
//        user.setName(request.getName());
//        user.setDob(request.getDob());
        userMapper.updateUser(user,request); //1 dong nay dung mapper thay cho 3 dong tren

        return userRepository.save(user);
    }

    //xoa user theo id
    public void deleteUser(String id){
        userRepository.deleteById(id);
    }
    //kiem tra user
    public boolean existsById(String id) {
        return userRepository.existsById(id);
    }

}
