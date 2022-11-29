package com.blog.api.controller;

import com.blog.api.domain.Post;
import com.blog.api.repository.PostRepository;
import com.blog.api.request.PostCreate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(value = RestDocumentationExtension.class)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.blog.com", uriPort = 443)
@SpringBootTest
public class PostControllerDocsTest {

    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    @DisplayName("글 단건 조회")
    void test1() throws Exception {
        // given
        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(post);

        // expected
        mockMvc.perform(RestDocumentationRequestBuilders.get("/posts/{postId}", post.getId())
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("post-inquiry", pathParameters(
                                parameterWithName("postId").description("게시물 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("게시글 ID"),
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("content").description("내용")
                        )
                ));
    }

    @Test
    @DisplayName("글 등록")
    void test2() throws Exception {

        /**
         * contentType, accept, content, characterEncoding 차이 확인
         */

        // given
        PostCreate request = PostCreate.builder()
                .title("사리")
                .content("반포")
                .build();

        // expected
        mockMvc.perform(post("/v2/test")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .characterEncoding("UTF-8")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("post-create", requestFields(
                                fieldWithPath("title").description("제목")
                                        .attributes(Attributes.key("constraint").value("좋은 제목 입력해주세요")), // custom field
                                fieldWithPath("content").description("내용").optional() // restDocs에서 제공하는 optional 기능
                        )
                ));
    }

}
