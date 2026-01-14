package com.portfolio.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portfolio.model.Alert;
import com.portfolio.model.Like;
import com.portfolio.model.Post;
import com.portfolio.model.User;
import com.portfolio.repository.AlertRepository;
import com.portfolio.repository.LikeRepository;
import com.portfolio.repository.PostRepository;
import com.portfolio.repository.UserRepository;

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
        // 좋아요 취소 처리
        Optional<Like> existing = likeRepository.findByUserIdAndPostId(userId, postId);
        if(existing.isPresent()) {
            // 좋아요 취소
            likeRepository.delete(existing.get());

            // 삭제할 알림 content
            String content;
            if(post.getContent().length() > 20) {
                content = user.getNickname() + "님이 " + post.getContent().substring(0, 20) + "... 게시글을 좋아합니다.";
            } else {
                content = user.getNickname() + "님이 " + post.getContent() + "게시글을 좋아합니다.";
            }

            List<Alert> alerts = alertRepository.findByUserIdAndContent(post.getUser().getId(), content);
            if(!alerts.isEmpty()) {
                alertRepository.deleteAll(alerts);
            }

            
            return "already"; 
        }else{
            // 좋아요 저장
            Like like = new Like();
            like.setUser(user);
            like.setPost(post);
            likeRepository.save(like);

            // 알림 생성
            Alert alert = new Alert();
            alert.setUserId(post.getUser().getId());
            if (post.getContent().length() > 20) {
                alert.setContent(user.getNickname() + "님이 " + post.getContent().substring(0, 20) + "... 게시글을 좋아합니다.");
            } else {
                alert.setContent(user.getNickname() + "님이 " + post.getContent() + "게시글을 좋아합니다.");
            }
            alertRepository.save(alert);

            return "success";
        }
    }
}
