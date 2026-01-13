package com.portfolio.controller;

import org.springframework.web.bind.annotation.*;

import com.portfolio.dto.CommentDto;
import com.portfolio.model.Comment;
import com.portfolio.model.User;
import com.portfolio.service.AlertService;
import com.portfolio.service.CommentService;
import java.util.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final AlertService alertService;

    public CommentController(CommentService commentService, AlertService alertService) {
        this.commentService = commentService;
        this.alertService = alertService;
    }

    // 댓글 리스트
    @GetMapping("/{postId}")
    public List<CommentDto> getComments(@PathVariable Long postId) {
        return commentService.getComments(postId);
    }
    
    // 댓글 작성
    @PostMapping("/saveComment")
    @ResponseBody
    public CommentDto writeComment(@RequestBody Map<String, String> request, HttpSession session){
        User loginUser = (User) session.getAttribute("loginUser");
        Long postId = Long.valueOf(request.get("postId"));
        String content = request.get("content");
        
        // 댓글 저장
        Comment comment = commentService.saveComment(postId, loginUser.getId(), content);

        // 알림 생성
        User postAuthor = comment.getPost().getUser(); // 댓글 단 게시글 작성자
        if(!postAuthor.getId().equals(loginUser.getId())) { // 본인 댓글이면 알림 X
            String postContent = comment.getPost().getContent();
            alertService.createCommentAlert(postAuthor, loginUser, postContent);
        }

        // DTO로 변환해서 반환
        return new CommentDto(
            comment.getId(),
            comment.getContent(),
            comment.getUser().getNickname(),  // 프록시 문제 방지
            comment.getCreatedAt()
        );
    }

}
