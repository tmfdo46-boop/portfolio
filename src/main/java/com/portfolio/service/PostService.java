package com.portfolio.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.portfolio.model.Post;
import com.portfolio.model.PostImage;
import com.portfolio.repository.PostImageRepository;
import com.portfolio.repository.PostRepository;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;

    public PostService(PostRepository postRepository, PostImageRepository postImageRepository) {
        this.postRepository = postRepository;
        this.postImageRepository = postImageRepository;
    }

    public List<Post> getPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }
    
    public Post save(Post post) {
        return postRepository.save(post);
    }
    
    public void savePostImages(List<PostImage> images) {
        postImageRepository.saveAll(images);
    }

    public List<Post> findAll() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }
    
    public Post findById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다. id=" + postId));
    }
}