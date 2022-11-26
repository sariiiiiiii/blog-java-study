package com.blog.api.exception;

public class InvalidRequest extends PostException {

    private static final String MESSAGE = "잘못된 요청입니다.";

    public InvalidRequest(String field, String message) {
        super(MESSAGE);
        addValidation(field, message);
    }

    @Override
    public int getStatus() {
        return 400;
    }
}
