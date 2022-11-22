package com.blog.api.controller;

import com.blog.api.request.PostCreate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class PostController {

    @GetMapping("/gets")
    public String get() {
        return "hello";
    }

    @PostMapping("/posts")
    public Map<String, String> post(@RequestBody @Valid PostCreate postCreate, BindingResult result) {
        if (result.hasErrors()) {
            List<FieldError> errors = result.getFieldErrors();
            FieldError error = errors.get(0);
            String field = error.getField();
            String message = error.getDefaultMessage();
            Map<String, String> response = new HashMap<>();
            response.put(field, message);
            return response;
        }
        return Map.of();
    }

}
