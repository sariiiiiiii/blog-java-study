package com.blog.api.service;

import com.blog.api.domain.Post;
import com.blog.api.repository.PostRepository;
import com.blog.api.request.PostCreate;
import com.blog.api.request.PostSearch;
import com.blog.api.response.PostResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.domain.Sort.Direction.DESC;

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

    @Test
    @DisplayName("paging을 이용한 여러건 조회(spring data jpa)")
    void getListPaging() throws Exception {

        // given
        List<Post> postRequest = IntStream.range(1, 31)
                .mapToObj(i -> {
                    return Post.builder()
                            .title("블로그 제목 " + i)
                            .content("반포 자이 " + i)
                            .build();
                })
                .collect(Collectors.toList());
        postRepository.saveAll(postRequest);

        // when
        Pageable pageRequest = PageRequest.of(0, 5, DESC, "id");
        List<PostResponse> response = postService.getListPaging(pageRequest);

        // then
        assertEquals(response.size(), 5L);
        assertEquals(response.get(0).getTitle(), "블로그 제목 30");
        assertEquals(response.get(0).getContent(), "반포 자이 30");
    }

    @Test
    @DisplayName("여러건 조회 (querydsl)")
    void getListQuerydslPaging() throws Exception {

        // given
        List<Post> postRequest = IntStream.range(1, 31)
                .mapToObj(i -> {
                    return Post.builder()
                            .title("블로그 제목 " + i)
                            .content("반포 자이 " + i)
                            .build();
                })
                .collect(Collectors.toList());
        postRepository.saveAll(postRequest);

        // when
        Pageable pageable = PageRequest.of(1, 10, DESC, "id");
        List<PostResponse> response = postService.getListQuerydslPaging(pageable);

        // then
        assertEquals(10L, response.size());
        assertEquals(response.get(0).getId(), 20);
        assertEquals(response.get(0).getTitle(), "블로그 제목 20");
        assertEquals(response.get(0).getContent(), "반포 자이 20");
    }

    @Test
    @DisplayName("여러건 조회 (PostSearch + querydsl)")
    void getListPostSearchPaging() throws Exception {

        // given
        List<Post> postRequest = IntStream.range(1, 31)
                .mapToObj(i -> {
                    return Post.builder()
                            .title("블로그 제목 " + i)
                            .content("반포 자이 " + i)
                            .build();
                })
                .collect(Collectors.toList());
        postRepository.saveAll(postRequest);

        // when
        PostSearch postSearch = PostSearch.builder()
                .page(2)
                .size(10)
                .build();
        List<PostResponse> response = postService.getListPostSearchPaging(postSearch);

        // then
        assertEquals(10L, response.size());
        assertEquals(20, response.get(0).getId());
        assertEquals("블로그 제목 20", response.get(0).getTitle());
        assertEquals("반포 자이 20", response.get(0).getContent());

    }

}