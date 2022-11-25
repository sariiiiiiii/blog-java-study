package com.blog.api.repository;

import com.blog.api.domain.Post;
import com.blog.api.request.PostSearch;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.blog.api.domain.QPost.post;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Post> getListQuerydslPaging(Pageable pageable) {
        return queryFactory
                .selectFrom(post)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.id.desc())
                .fetch();
    }

    @Override
    public List<Post> getListPostSearchPaging(PostSearch postSearch) {
        return queryFactory
                .selectFrom(post)
                .offset(postSearch.getOffset())
                .limit(postSearch.getSize())
                .orderBy(post.id.desc())
                .fetch();
    }
}
