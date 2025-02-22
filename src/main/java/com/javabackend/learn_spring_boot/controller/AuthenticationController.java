package com.javabackend.learn_spring_boot.controller;

import com.javabackend.learn_spring_boot.dto.request.ApiResponse;
import com.javabackend.learn_spring_boot.dto.request.AuthenticationRequest;
import com.javabackend.learn_spring_boot.dto.request.UserCreatRequest;
import com.javabackend.learn_spring_boot.dto.response.AuthenticationResponse;
import com.javabackend.learn_spring_boot.model.User;
import com.javabackend.learn_spring_boot.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/log-in")
    public ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        boolean result = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(AuthenticationResponse.builder()
                        .authentication(result)
                        .build())
                .build();
    }

}
