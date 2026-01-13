package com.portfolio.service;

import com.portfolio.model.Alert;
import com.portfolio.model.User;
import com.portfolio.repository.AlertRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertService {

    private final AlertRepository alertRepository;

    public AlertService(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    // 댓글 알림 생성
    public void createCommentAlert(User postUser, User commentUser, String postContent) {
        // content 20자 이상이면 자르고 ... 붙이기
        String preview = postContent.length() > 20 ? postContent.substring(0, 20) + "..." : postContent;

        String content = commentUser.getNickname() + "님이 '" + preview + "'에 댓글을 남겼습니다.";
        Alert alert = new Alert(postUser, content);
        alertRepository.save(alert);
    }

    // 특정 사용자 알림 조회
    public List<Alert> getAlertsForUser(Long userId) {
        return alertRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    // 알림 ID로 조회
    public Alert findById(Long id) {
        return alertRepository.findById(id).orElseThrow(() -> new RuntimeException("알림 없음"));
    }

    // 알림 저장/수정
    public Alert save(Alert alert) {
        return alertRepository.save(alert);
    }

    // 읽음 처리
    public void markAsRead(Long id) {
        Alert alert = findById(id);
        alert.setReadYn("Y");
        save(alert);
    }
}
