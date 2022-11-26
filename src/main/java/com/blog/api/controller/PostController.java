package com.blog.api.controller;

import com.blog.api.domain.Post;
import com.blog.api.request.PostCreate;
import com.blog.api.request.PostEdit;
import com.blog.api.request.PostSearch;
import com.blog.api.response.PostResponse;
import com.blog.api.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/get")
    public String hello() {
        return "hello";
    }

    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable("postId") Long id) {
        return postService.get(id);
    }

    @GetMapping("/posts")
    public List<PostResponse> getList() {
        return postService.getList();
    }

    // PostNotFound 활용
    @GetMapping("/posts/v2/{postId}")
    public PostResponse get2(@PathVariable("postId") Long id) {
        return postService.get2(id);
    }

    // paging 처리용 여러건 조회(spring data jpa)
    @GetMapping("/v1/posts")
    public List<PostResponse> getListPaging(Pageable pageable) {
        return postService.getListPaging(pageable);
    }

    // paging 처리용 여러건 조회(querydsl)
    @GetMapping("/v2/posts")
    public List<PostResponse> getListQuerydslPaging(Pageable pageable) {
        return postService.getListQuerydslPaging(pageable);
    }

    // paging 처리용 여러건 조회(PostSearch dto + querydsl)
    @GetMapping("/v3/posts")
    public List<PostResponse> getListPostSearchPaging(@ModelAttribute PostSearch postSearch) {
        return postService.getListPostSearchPaging(postSearch);
    }

    @PostMapping("/posts")
    public Map<String, String> post(@RequestBody @Valid PostCreate postCreate) {
        return Map.of();
    }

    @PostMapping("/test")
    public Map<String, String> test(@RequestBody @Valid PostCreate postCreate) {
        postService.write(postCreate);
        return Map.of();
    }

    // invalidException 활용
    @PostMapping("/v2/test")
    public void invalidPost(@RequestBody @Valid PostCreate postCreate) {
        postCreate.validate();
        postService.write(postCreate);
    }

    /**
     * client에서 수정된 data와 기존의 data를 같이 보내게 될 경우 @Valid check 및 PostEdit 클리스에 validation annotation 추가 (@NotBlank)
     * 수정된 정보를 반환을 해주지 말지는 client와 협의
     */
    @PatchMapping("/posts/{postId}")
    public PostResponse edit(@PathVariable("postId") Long id, @RequestBody PostEdit postEdit) {
        return postService.edit(id, postEdit);
    }

    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable("postId") Long id) {
        postService.delete(id);
    }

}
