package com.javabackend.learn_spring_boot.exception;

import com.javabackend.learn_spring_boot.dto.request.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // kieu bat loi co ban
//    @ExceptionHandler(value = RuntimeException.class)
//    ResponseEntity<String> handlingRuntimeException(RuntimeException exception){
//        return ResponseEntity.badRequest().body(exception.getMessage());
//    }

    // lay tu kieu bat loi co ban la RuntimeException de bat loi nhung loi ko xac dinh
    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException exception){

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(AppException exception){

        // o trong class AppException phan tu chinh la ErrorCode nen h get ra de dung
        ErrorCode errorCode = exception.getErrorCode();
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException exception){
        //lay loi o message cua Annotation @
        String enumKey = exception.getFieldError().getDefaultMessage();

        // tao mac dinh errorCode = ErrorCode.INVALID_KEY
        ErrorCode errorCode = ErrorCode.INVALID_KEY;

        //neu error khong giong voi error lay o enumKey
        try {
            // lay gia tri enumKey o ErrorCode, neu khong co thi bat loi IllegalArgumentException va lay lai gia tri mac dinh
            errorCode = ErrorCode.valueOf(enumKey);
        } catch (IllegalArgumentException e){
            // xu ly neu co loi khi khong lay duoc Enum nao o ErrorCode
        }

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

}
