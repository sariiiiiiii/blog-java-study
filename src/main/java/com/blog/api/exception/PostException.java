package com.blog.api.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class PostException extends RuntimeException {

    public final Map<String, String> validation = new HashMap<>();

    public PostException(String message) {
        super(message);
    }

    public void addValidation(String field, String message) {
        validation.put(field, message);
    }

    public abstract int getStatus();

}
