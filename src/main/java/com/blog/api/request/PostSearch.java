package com.blog.api.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class PostSearch {

    private static final int MAX_SIZE = 2000;

    private Integer page;

    private Integer size;

    public PostSearch(Integer page, Integer size) {
        this.page = (page - 1) * size;
        this.size = size;
    }

    public long getOffset() {
        return (long) (Math.max(page, 1) - 1) * Math.min(size, MAX_SIZE);
    }

}
