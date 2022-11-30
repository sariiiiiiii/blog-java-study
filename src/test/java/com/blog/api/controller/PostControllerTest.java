package com.blog.api.controller;

import com.blog.api.domain.Post;
import com.blog.api.repository.PostRepository;
import com.blog.api.request.PostCreate;
import com.blog.api.request.PostEdit;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PostRepository postRepository;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void before() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("get test")
    void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/get"))
                .andExpect(status().isOk())
                .andExpect(content().string("hello"))
                .andDo(print());
    }

    @Test
    @DisplayName("post test")
    void post() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content("{\"title\": \"\", \"content\": \"콘텐츠 입니다.\"}")
                )
                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.code").value("400"))
//                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
//                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요"))
                .andDo(print());
    }

    @Test
    @DisplayName("DB에 값 저장")
    void post_v2() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/test")
                        .contentType(APPLICATION_JSON)
                        .content("{\"title\" :  \"타이틀 입니다.\", \"content\" : \"콘텐츠 입니다.\"}")
                )
                .andExpect(status().isOk())
                .andExpect(content().string("{}"))
                .andDo(print());
    }

    @Test
    @DisplayName("DB에 값 저장")
    void test() throws Exception {
        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/test")
                        .contentType(APPLICATION_JSON)
                        .content("{\"title\": \"타이틀 입니다.\", \"content\": \"콘텐츠 입니다.\"}")
                )
                .andExpect(status().isOk())
                .andDo(print());

        // then
        assertEquals(1L, postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("타이틀 입니다.", post.getTitle());
        assertEquals("콘텐츠 입니다.", post.getContent());
    }

    @Test
    @DisplayName("ObjectMapper를 이용한 DB값 저장")
    void objectMapper() throws Exception {

        // given
        PostCreate postCreate = PostCreate.builder()
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(postCreate);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("@Builder에 length 길이 감지 추가")
    void postResponse() throws Exception {

        // given
        Post post = postRepository.save(Post.builder()
                .title("123456789012345")
                .content("bar")
                .build());

        // expected
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("1234567890"))
                .andExpect(jsonPath("$.content").value("bar"))
                .andDo(print());
    }

    @Test
    @DisplayName("단 건 조회")
    void test2() throws Exception {

        //given
        Post post = Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        postRepository.save(post);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value(post.getTitle()))
                .andExpect(jsonPath("$.content").value(post.getContent()))
                .andDo(print());
    }

    @Test
    @DisplayName("여러건 조회")
    void getList() throws Exception {

        // given
        Post post1 = postRepository.save(Post.builder()
                .title("title1")
                .content("content1")
                .build());

        Post post2 = postRepository.save(Post.builder()
                .title("title2")
                .content("content2")
                .build());

        // expected
        mockMvc.perform(MockMvcRequestBuilders.get("/posts")
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].id").value(post1.getId()))
                .andExpect(jsonPath("$[0].title").value(post1.getTitle()))
                .andExpect(jsonPath("$[0].content").value(post1.getContent()))
                .andExpect(jsonPath("$[1].id").value(post2.getId()))
                .andExpect(jsonPath("$[1].title").value(post2.getTitle()))
                .andExpect(jsonPath("$[1].content").value(post2.getContent()))
                .andDo(print());
    }

    @Test
    @DisplayName("paging을 이용한 여러건 조회 (spring data jpa)")
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

        // expected
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/posts?page=0&size=10")
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
                .andExpect(jsonPath("$[0].id").value(65L))
                .andExpect(jsonPath("$[0].title").value("블로그 제목 1"))
                .andExpect(jsonPath("$[0].content").value("반포 자이 1"))
                .andDo(print());
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

        // expected
        mockMvc.perform(MockMvcRequestBuilders.get("/v2/posts?page=1&size=10")
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
                .andExpect(jsonPath("$[0].id").value(51L))
                .andExpect(jsonPath("$[0].title").value("블로그 제목 20"))
                .andExpect(jsonPath("$[0].content").value("반포 자이 20"))
                .andDo(print());
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

        // expected
        mockMvc.perform(MockMvcRequestBuilders.get("/v3/posts?page=3&size=10")
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
                .andExpect(jsonPath("$[0].id").value(10))
                .andExpect(jsonPath("$[0].title").value("블로그 제목 10"))
                .andExpect(jsonPath("$[0].content").value("반포 자이 10"))
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 수정")
    void edit() throws Exception {
        //given
        Post requestPost = Post.builder()
                .title("사리")
                .content("반포자이")
                .build();

        postRepository.save(requestPost);

        PostEdit postEdit = PostEdit.builder()
                .title("사리맨")
                .content("자이반포")
                .build();

        //expected
        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/{postId}", requestPost.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit))
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("글 삭제")
    void postDelete() throws Exception {
        //given
        Post post = Post.builder()
                .title("사리")
                .content("반포자이")
                .build();
        postRepository.save(post);

        //expected
        mockMvc.perform(MockMvcRequestBuilders.delete("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 글 조회")
    void getPostNotFound() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/posts/v2/{postId}", 1L)
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 작성시 바보는 포함될 수 없다")
    void postInvalidRequest() throws Exception {

        PostCreate request = PostCreate.builder()
                .title("바보사리")
                .content("반포자이")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/v2/test")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("가장 기본적인 요청 인증값 확인 (GET Parameter)")
    void getParameterAuthorization() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .title("사리")
                .content("내용")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/v3/test?authorization=sari")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());

        // then
        assertEquals(1L, postRepository.count());
    }

    @Test
    @DisplayName("가장 기본적인 요청 인증값 확인 (Header)")
    void headerAuthorization() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .title("사리")
                .content("내용")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/v4/test")
                        .header("authorization", "header")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());

        // then
        assertEquals(1L, postRepository.count());
    }

}