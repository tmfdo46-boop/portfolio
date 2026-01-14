package com.portfolio.service;

import com.portfolio.dto.CommentDto;

import com.portfolio.model.Alert;
import com.portfolio.model.Comment;
import com.portfolio.model.Post;
import com.portfolio.model.User;

import com.portfolio.repository.CommentRepository;
import com.portfolio.repository.PostRepository;
import com.portfolio.repository.UserRepository;
import com.portfolio.repository.AlertRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AlertRepository alertRepository;

    public CommentService(CommentRepository commentRepository,
                          PostRepository postRepository,
                          UserRepository userRepository,
                          AlertRepository alertRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.alertRepository = alertRepository;
    }

    // 댓글 조회
    public List<CommentDto> getComments(Long postId) {
        return commentRepository.findByPostId(postId)
                .stream()
                .map(c -> new CommentDto(
                        c.getId(),
                        c.getContent(),
                        c.getUser().getNickname(),  // authorName
                        c.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    // 댓글 작성
    public Comment saveComment(Long postId, Long userId, String content) {
        Optional<Post> postOpt = postRepository.findById(postId);
        Optional<User> userOpt = userRepository.findById(userId);

        if(postOpt.isEmpty() || userOpt.isEmpty()) {
            throw new IllegalArgumentException("게시글 또는 사용자 정보가 올바르지 않습니다.");
        }

        Comment comment = new Comment(postOpt.get(), userOpt.get(), content);
        return commentRepository.save(comment);
    }

    // 댓글 알림 생성
    public void createCommentAlert(User postUser, User commentUser, String postContent) {
        // content 20자 이상이면 자르고 ... 붙이기
        String preview = postContent.length() > 20 ? postContent.substring(0, 20) + "..." : postContent;

        String content = commentUser.getNickname() + "님이 '" + preview + "'에 댓글을 남겼습니다.";
        Alert alert = new Alert(postUser.getId(), content);
        alertRepository.save(alert);
    }
}