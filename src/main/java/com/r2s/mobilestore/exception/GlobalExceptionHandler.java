package com.r2s.mobilestore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.xml.bind.ValidationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        // Xử lý và ghi log ngoại lệ
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Internal Server Error: " + e.getMessage());
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        // Xử lý và ghi log ngoại lệ khi không tìm thấy đối tượng
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Resource not found: " + e.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidationException(ValidationException e) {
        // Xử lý và ghi log ngoại lệ liên quan đến việc kiểm tra dữ liệu đầu vào
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Validation error: " + e.getMessage());
    }

    @ExceptionHandler(DuplicatedCategoryNameException.class)
    public ResponseEntity<String> handleDuplicatedCategoryNameException(DuplicatedCategoryNameException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(e.getMessage());
    }


    // Xử lý các ngoại lệ khác tại đây
}
