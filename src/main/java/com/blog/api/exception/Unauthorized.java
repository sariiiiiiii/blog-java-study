package com.blog.api.exception;

public class Unauthorized extends PostException {

    private static final String MESSAGE = "인증이 필요합니다.";

    public Unauthorized() {
        super(MESSAGE);
    }

    public Unauthorized(String field, String message) {
        super(MESSAGE);
        addValidation(field, message);
    }

    @Override
    public int getStatus() {
        return 401;
    }
}
