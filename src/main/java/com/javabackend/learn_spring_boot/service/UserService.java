package com.javabackend.learn_spring_boot.service;

import com.javabackend.learn_spring_boot.dto.request.UserCreatRequest;
import com.javabackend.learn_spring_boot.dto.request.UserUpdateRequest;
import com.javabackend.learn_spring_boot.exception.AppException;
import com.javabackend.learn_spring_boot.exception.ErrorCode;
import com.javabackend.learn_spring_boot.model.User;
import com.javabackend.learn_spring_boot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    //danh sach user
    public List<User> getUsers() {
        return userRepository.findAll();
    }
    //1 user theo id
    public User getUserByUserId(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    //1 user theo userName
    public Optional<User> getUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }
    //them user
    public User createUser(UserCreatRequest userRequest){
        User user = new User();

        user.setUserName(userRequest.getUserName());
        if(userRepository.existsByUserName(userRequest.getUserName()))
            throw new AppException(ErrorCode.USER_EXISTED);

        user.setPassword(userRequest.getPassword());
        user.setName(userRequest.getName());
        user.setDob(userRequest.getDob());
        return userRepository.save(user);
    }

    // cap nhat user
    public User updateUser(String userId, UserUpdateRequest request) {
        User user = getUserByUserId(userId);

        user.setPassword(request.getPassword());
        user.setName(request.getName());
        user.setDob(request.getDob());

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
