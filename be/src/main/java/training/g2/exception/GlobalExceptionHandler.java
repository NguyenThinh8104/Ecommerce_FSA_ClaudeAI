package training.g2.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import training.g2.exception.common.BusinessException;
import training.g2.model.ApiResponse;

import static training.g2.constant.Constants.Message.INVENTORY_OPTIMISTIC_LOCK_FAIL;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

        @ExceptionHandler(Exception.class)
        @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        public ResponseEntity<ApiResponse<Object>> handleOverallException(Exception ex) {
                log.error(" Lỗi hệ thống:", ex);
                ApiResponse<Object> response = new ApiResponse<>(
                                "Đã xảy ra lỗi trong hệ thống",
                                ex.getMessage());
                return ResponseEntity.internalServerError().body(response);
        }

        @ExceptionHandler(BusinessException.class)
        public ResponseEntity<ApiResponse<Object>> handleBusinessException(BusinessException be) {
                log.warn(" Lỗi nghiệp vụ: {}", be.getResponseMessage());
                ApiResponse<Object> response = new ApiResponse<>(
                                be.getResponseMessage(),
                                be.getResponseData());
                return ResponseEntity.status(be.getHttpCode()).body(response);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
        public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationException(
                        MethodArgumentNotValidException ex) {
                log.warn(" Lỗi validate: {}", ex.getMessage());
                Map<String, String> errors = new HashMap<>();
                ex.getBindingResult().getFieldErrors()
                                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

                ApiResponse<Map<String, String>> response = new ApiResponse<>(
                                "Dữ liệu không hợp lệ",
                                errors);
                return ResponseEntity.badRequest().body(response);
        }

        @ExceptionHandler(ConstraintViolationException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ResponseEntity<ApiResponse<String>> handleConstraintViolationException(ConstraintViolationException ex) {
                log.warn("Vi phạm ràng buộc dữ liệu: {}", ex.getMessage());
                ApiResponse<String> response = new ApiResponse<>(
                                "Tham số yêu cầu không hợp lệ",
                                ex.getMessage());
                return ResponseEntity.badRequest().body(response);
        }

        @ExceptionHandler(AccessDeniedException.class)
        @ResponseStatus(HttpStatus.FORBIDDEN)
        public ResponseEntity<ApiResponse<String>> handleAccessDeniedException(AccessDeniedException ex) {
                ApiResponse<String> response = new ApiResponse<>(
                                "Không có quyền truy cập tài nguyên này",
                                ex.getMessage());
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

}
