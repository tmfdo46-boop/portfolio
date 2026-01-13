package com.portfolio.service;

import com.portfolio.model.Alert;
import com.portfolio.model.Like;
import com.portfolio.model.Post;
import com.portfolio.model.User;

import com.portfolio.repository.AlertRepository;
import com.portfolio.repository.LikeRepository;
import com.portfolio.repository.PostRepository;
import com.portfolio.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AlertRepository alertRepository;

    public LikeService(LikeRepository likeRepository, PostRepository postRepository,
                       UserRepository userRepository, AlertRepository alertRepository) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.alertRepository = alertRepository;
    }

    @Transactional
    public String likePost(Long postId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Post post = postRepository.findById(postId).orElseThrow();

        // 이미 좋아요 눌렀는지 체크
        if(likeRepository.existsByUserAndPost(user, post)) {
            return "already";
        }

        // 좋아요 저장
        Like like = new Like();
        like.setUser(user);
        like.setPost(post);
        likeRepository.save(like);

        // 알림 생성
        Alert alert = new Alert();
        alert.setUser(post.getUser()); // 게시글 작성자
        alert.setContent(user.getNickname() + "님이 회원님의 게시글을 좋아합니다.");
        alertRepository.save(alert);

        return "success";
    }
}
