package com.portfolio.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.portfolio.model.Post;
import com.portfolio.repository.PostRepository;

@Service
public class PostService {
    private final PostRepository postRepository;
    public PostService(PostRepository postRepository) { this.postRepository = postRepository; }

    public List<Post> getPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }
}