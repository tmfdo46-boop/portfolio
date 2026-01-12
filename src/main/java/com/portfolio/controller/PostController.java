package com.portfolio.controller;

import com.portfolio.dto.PostDetailDto;
import com.portfolio.dto.PostResponseDto;
import com.portfolio.model.Post;
import com.portfolio.model.PostImage;
import com.portfolio.model.User;

import com.portfolio.service.PostService;
import com.portfolio.service.FollowService;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final FollowService followService;

    public PostController(PostService postService, FollowService followService) {
        this.postService = postService;
        this.followService = followService;
    }

    @GetMapping("/list")
    @ResponseBody
    public List<PostResponseDto> getPosts(HttpSession session){
        User loginUser = (User) session.getAttribute("loginUser");
        return postService.getAllPostsWithFollow(loginUser);
    }

    // 글쓰기 fragment
    @GetMapping("/write")
    public String writePage() {
        return "postWrite";
    }

    // 게시글 작성
    @PostMapping("/write")
    @ResponseBody
    public PostResponseDto writePost(
        @RequestParam("content") String content,
        @RequestParam(value = "images", required = false) List<MultipartFile> images,
        HttpSession session
    ) throws IOException {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) throw new RuntimeException("로그인 필요");

        Post post = new Post();
        post.setContent(content);
        post.setUser(loginUser);

        // 게시글 먼저 저장 → ID 필요
        Post savedPost = postService.save(post);

        // 이미지 저장
        if (images != null) {
            for (MultipartFile file : images) {
                if (file.isEmpty()) continue;

                // 오늘날짜 + 랜덤4자리 + 원본파일명
                LocalDate now = LocalDate.now();
                String today = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                int randomNum = new Random().nextInt(9000) + 1000;
                String fileName = today + "_" + randomNum + "_" + file.getOriginalFilename();

                // 현재 날짜 기준 년/월 폴더
                String year = String.valueOf(now.getYear());
                String month = String.format("%02d", now.getMonthValue());

                // 서버 저장 경로
                Path folderPath = Paths.get("C:/upload", year, month);
                if (!Files.exists(folderPath)) {
                    Files.createDirectories(folderPath);
                }

                Path filePath = folderPath.resolve(fileName);
                file.transferTo(filePath.toFile());

                // DB에 경로 저장 (웹 접근용)
                String dbPath = "/upload/" + year + "/" + month + "/" + fileName;
                PostImage postImage = new PostImage(savedPost, dbPath);

                // Post 엔티티에 연결
                savedPost.getImages().add(postImage);
            }

            // 변경된 이미지 리스트 저장
            postService.save(savedPost);
        }

        return new PostResponseDto(savedPost);
    }
    
    @PostMapping("/like/{postId}")
    @ResponseBody
    public PostResponseDto likePost(
            @PathVariable Long postId,
            @RequestBody Map<String, Boolean> data
    ) {
        boolean like = data.get("like"); // true: 좋아요, false: 취소
        Post post = postService.findById(postId);

        if (like) post.incrementLike();
        else post.decrementLike();

        postService.save(post);
        return new PostResponseDto(post);
    }
    
    // 게시글 상세
    @GetMapping("/detail/{id}")
    public PostDetailDto getPostDetail(@PathVariable Long id, HttpSession session) {
        Post post = postService.findById(id);
        User loginUser = (User) session.getAttribute("loginUser");

        boolean isFollowing = false;
        if (loginUser != null && !loginUser.getId().equals(post.getUser().getId())) {
            isFollowing = followService.isFollowing(loginUser.getId(), post.getUser().getId());
        }

        return new PostDetailDto(post, isFollowing);
    }
}
