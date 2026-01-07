package com.portfolio.controller;

import com.portfolio.dto.PostDTO;
import com.portfolio.model.Post;
import com.portfolio.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // 게시글 목록 조회 (DTO 사용)
    @GetMapping("/list")
    public List<PostDTO> list() {
        List<Post> posts = postService.getPosts();
        return posts.stream()
                    .map(PostDTO::new) // 엔티티 → DTO 변환
                    .collect(Collectors.toList());
    }

    // 글쓰기 화면 (Fragment)
    @GetMapping("/write")
    public String writePage() {
        return "postWrite"; // postWrite.html 또는 JSP
    }

    // 글쓰기 처리
    // @PostMapping("/write") ... (이미지 업로드 포함 처리)
}
