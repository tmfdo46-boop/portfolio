package com.portfolio.controller;

import com.portfolio.dto.PostResponseDto;

import com.portfolio.model.Post;
import com.portfolio.model.User;

import com.portfolio.service.LikeService;
import com.portfolio.service.PostService;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/likes")
public class LikeController {

    private final LikeService likeService;
    private final PostService postService;

    public LikeController(LikeService likeService, PostService postService) {
        this.likeService = likeService;
        this.postService = postService;
    }

    @PostMapping("/post/{postId}")
    public PostResponseDto likePost(@PathVariable Long postId, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");

        // User.id 기준으로 좋아요 처리
        likeService.likePost(postId, loginUser.getId());

        // 최신 likeCount 반환
        Post post = postService.findById(postId);
        return new PostResponseDto(post);
    }
}

