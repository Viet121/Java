package com.javabackend.learn_spring_boot.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javabackend.learn_spring_boot.dto.request.ApiResponse;
import com.javabackend.learn_spring_boot.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    //Lớp này ghi đè phương thức commence() để xử lý lỗi xác thực.
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        //Thiết lập mã lỗi và kiểu dữ liệu JSON
        ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;
        response.setStatus(errorCode.getHttpStatus().value());
        //đảm bảo phản hồi ở định dạng JSON.
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        //Tạo phản hồi
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();

        ObjectMapper objectMapper = new ObjectMapper();

        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        response.flushBuffer();
    }
}
