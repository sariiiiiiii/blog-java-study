package com.blog.api.exception;

import lombok.Getter;

@Getter
public class PostNotFound extends PostException {

    private static final String MESSAGE = "존재하지 않는 글입니다.";

    public PostNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatus() {
        return 404;
    }
}
