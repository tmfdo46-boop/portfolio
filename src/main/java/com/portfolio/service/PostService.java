package com.portfolio.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.portfolio.dto.PostResponseDto;
import com.portfolio.model.Post;
import com.portfolio.model.PostImage;
import com.portfolio.model.User;
import com.portfolio.repository.PostImageRepository;
import com.portfolio.repository.PostRepository;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final FollowService followService;

    public PostService(PostRepository postRepository, PostImageRepository postImageRepository, FollowService followService) {
        this.postRepository = postRepository;
        this.postImageRepository = postImageRepository;
        this.followService = followService;
    }

    // 단건 게시글 조회
    public Post getPost(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음"));
    }
    
    public Post save(Post post) {
        return postRepository.save(post);
    }
    
    public void savePostImages(List<PostImage> images) {
        postImageRepository.saveAll(images);
    }
    
    public Post findById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다. id=" + postId));
    }

    public List<PostResponseDto> getAllPostsWithFollow(User loginUser) {
        final List<Long> followingIds = loginUser == null
                ? List.of()
                : followService.getFollowingUsers(loginUser.getId())
                               .stream()
                               .map(User::getId)
                               .toList();

        return postRepository.findAllByOrderByCreatedAtDesc().stream()
            .map(post -> {
                boolean isFollowing = false;
                if (loginUser != null && !post.getUser().getId().equals(loginUser.getId())){
                    isFollowing = followingIds.contains(post.getUser().getId());
                }
                return new PostResponseDto(post, isFollowing, loginUser != null ? loginUser.getId() : null);
            })
            .collect(Collectors.toList());
    }
}