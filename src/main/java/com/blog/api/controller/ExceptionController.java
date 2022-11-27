package com.blog.api.controller;

import com.blog.api.exception.InvalidRequest;
import com.blog.api.exception.PostException;
import com.blog.api.exception.PostNotFound;
import com.blog.api.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse invalidRequestHandler(MethodArgumentNotValidException e) {
        ErrorResponse response = ErrorResponse.builder()
                .code("400")
                .message("잘못된 요청입니다.")
                .build();
        for (FieldError error : e.getFieldErrors()) {
            response.addValidation(error.getField(), error.getDefaultMessage());
        }
        return response;
    }

    @ExceptionHandler(PostException.class)
    public ResponseEntity<ErrorResponse> postException(PostException e) {

        ErrorResponse body = ErrorResponse.builder()
                .code(String.valueOf(e.getStatus()))
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();

        return ResponseEntity.status(e.getStatus())
                .body(body);
    }

}
