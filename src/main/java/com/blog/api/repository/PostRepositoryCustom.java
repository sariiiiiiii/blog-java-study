package com.blog.api.repository;

import com.blog.api.domain.Post;
import com.blog.api.request.PostSearch;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getListQuerydslPaging(Pageable pageable);

    List<Post> getListPostSearchPaging(PostSearch postSearch);

}
