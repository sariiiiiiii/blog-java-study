package com.blog.api.controller;

import com.blog.api.domain.Post;
import com.blog.api.request.PostCreate;
import com.blog.api.response.PostResponse;
import com.blog.api.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @PostMapping("/posts")
    public Map<String, String> post(@RequestBody @Valid PostCreate postCreate) {
        return Map.of();
    }

    @PostMapping("/test")
    public Map<String, String> test(@RequestBody @Valid PostCreate postCreate) {
        postService.write(postCreate);
        return Map.of();
    }

}
