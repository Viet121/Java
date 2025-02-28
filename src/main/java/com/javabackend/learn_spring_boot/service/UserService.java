package com.javabackend.learn_spring_boot.service;

import com.javabackend.learn_spring_boot.dto.request.UserCreatRequest;
import com.javabackend.learn_spring_boot.dto.request.UserUpdateRequest;
import com.javabackend.learn_spring_boot.dto.response.RoleResponse;
import com.javabackend.learn_spring_boot.dto.response.UserResponse;
import com.javabackend.learn_spring_boot.enums.Role;
import com.javabackend.learn_spring_boot.exception.AppException;
import com.javabackend.learn_spring_boot.exception.ErrorCode;
import com.javabackend.learn_spring_boot.mapper.UserMapper;
import com.javabackend.learn_spring_boot.model.User;
import com.javabackend.learn_spring_boot.repository.RoleRepository;
import com.javabackend.learn_spring_boot.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
// 2 dong duoi giup ta khoi viet annotation khi su dung cac repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    //danh sach user
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<User> getUsers() {
        return userRepository.findAll();
    }
    //1 user theo id
    //@Post la thoa dk moi thuc hien method, con @Pre la thu hien method xong kt dk moi return
    @PostAuthorize("returnObject.userName == authentication.name")
    public User getUserByUserId(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }
    //1 user theo token
    public UserResponse getMyInfo(){
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUserName(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserResponse(user);
    }
    //1 user theo userName
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public UserResponse getUserByUserName(String userName) {

        return userMapper.toUserResponse(userRepository.findByUserName(userName)
                .orElseThrow(() -> new RuntimeException("User not found")));
    }
    //them user
    public UserResponse createUser(UserCreatRequest userRequest){

        if(userRepository.existsByUserName(userRequest.getUserName()))
            throw new AppException(ErrorCode.USER_EXISTED);

//        User user = new User();
//        user.setUserName(userRequest.getUserName());
//        user.setPassword(userRequest.getPassword());
//        user.setName(userRequest.getName());
//        user.setDob(userRequest.getDob());
        User user = userMapper.toUser(userRequest); //1 dong nay dung mapper thay cho 5 dong tren
//      Vi su dung passwoedEncoder lai nhieu lan nen da tao @Bean passwoedEncoder o SecurityConfig
//        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        var roles = roleRepository.findAllById(userRequest.getRoles());
        user.setRoles(new HashSet<>(roles));
//        HashSet<String> roles = new  HashSet<>();
//        roles.add(Role.USER.name());
//        user.setRoles(roles);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    // cap nhat user
    public User updateUser(String userId, UserUpdateRequest request) {
        // tim va tao User tu userId
        User user = getUserByUserId(userId); //ham tim user o ben tren

//        user.setPassword(request.getPassword());
//        user.setName(request.getName());
//        user.setDob(request.getDob());
        userMapper.updateUser(user,request); //1 dong nay dung mapper thay cho 3 dong tren
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

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
