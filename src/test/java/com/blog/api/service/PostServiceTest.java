package com.blog.api.service;

import com.blog.api.domain.Post;
import com.blog.api.repository.PostRepository;
import com.blog.api.request.PostCreate;
import com.blog.api.response.PostResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void before() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void write() {

        // given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        // when
        postService.write(postCreate);
        Post post = postRepository.findAll().get(0);

        // then
        assertEquals(1L, postRepository.count());
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("단건 조회")
    void get() {

        // given
        Post request = Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        postRepository.save(request);

        PostResponse response = postService.get(request.getId());

        // then
        assertNotNull(response);
        assertEquals(1L, postRepository.count());
        assertEquals(request.getTitle(), response.getTitle());
        assertEquals(request.getContent(), response.getContent());
    }

    @Test
    @DisplayName("여러건 조회")
    void getList() throws Exception {

        // given
        postRepository.saveAll(List.of(
                Post.builder()
                        .title("title1")
                        .content("content")
                        .build(),
                Post.builder()
                        .title("title2")
                        .content("content2")
                        .build()
        ));

        // when
        List<PostResponse> list = postService.getList();

        //then
        assertEquals(2L, list.size());
    }

}