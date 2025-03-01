package com.javabackend.learn_spring_boot.exception;

import com.javabackend.learn_spring_boot.dto.request.ApiResponse;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.Objects;


@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final String MIN_ATTRIBUTE = "min";

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

        return ResponseEntity.status(errorCode.getHttpStatus()).body(apiResponse);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handlingAccessDeniedException(AccessDeniedException exception){
            ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

            return ResponseEntity.status(errorCode.getHttpStatus()).body(
                    ApiResponse.builder()
                            .code(errorCode.getCode())
                            .message(errorCode.getMessage())
                            .build()
            );
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException exception){
        //lay loi o message cua Annotation @
        String enumKey = exception.getFieldError().getDefaultMessage();

        // tao mac dinh errorCode = ErrorCode.INVALID_KEY
        ErrorCode errorCode = ErrorCode.INVALID_KEY;

        Map<String, Object> attributes = null;

        //neu error khong giong voi error lay o enumKey
        try {
            // lay gia tri enumKey o ErrorCode, neu khong co thi bat loi IllegalArgumentException va lay lai gia tri mac dinh
            errorCode = ErrorCode.valueOf(enumKey);

            /*
            exception.getBindingResult(): Lấy kết quả của quá trình kiểm tra dữ liệu (BindingResult).
            .getAllErrors().getFirst(): Lấy lỗi đầu tiên trong danh sách lỗi.
            .unwrap(ConstraintViolation.class): Chuyển đổi lỗi đó thành một đối tượng ConstraintViolation.*/
            var constraintViolation = exception.getBindingResult()
                    .getAllErrors().getFirst().unwrap(ConstraintViolation.class);
            /*constraintViolation.getConstraintDescriptor(): Lấy mô tả về ràng buộc bị vi phạm.
            .getAttributes(): Lấy danh sách thuộc tính của ràng buộc đó.
            Mục đích: Lấy các thông tin liên quan đến ràng buộc, như message, min, max, v.v.*/
            attributes = constraintViolation.getConstraintDescriptor().getAttributes();

            log.info(attributes.toString());

        } catch (IllegalArgumentException e){
            // xu ly neu co loi khi khong lay duoc Enum nao o ErrorCode
        }

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(Objects.nonNull(attributes) ?
                mapAttribute(errorCode.getMessage(), attributes)
                : errorCode.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    private String mapAttribute(String message, Map<String, Object> attributes){
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));

        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
    }

}
