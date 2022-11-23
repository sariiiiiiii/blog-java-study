package com.blog.api.controller;

import com.blog.api.request.PostCreate;
import com.blog.api.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/gets")
    public String get() {
        return "hello";
    }

    @PostMapping("/posts")
    public Map<String, String> post(@RequestBody @Valid PostCreate postCreate) {
        return Map.of();
    }

    @PostMapping("/test")
    public Map<String, String> test(@RequestBody @Valid PostCreate postCreate) {
        postService.save(postCreate);
        return Map.of();
    }

}
