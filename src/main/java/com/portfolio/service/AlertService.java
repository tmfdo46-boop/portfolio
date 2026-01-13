package com.portfolio.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.portfolio.model.Alert;
import com.portfolio.repository.AlertRepository;

@Service
public class AlertService {

    private final AlertRepository alertRepository;

    public AlertService(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    // 특정 사용자 알림 조회
    public List<Alert> getAlertsForUser(Long userId) {
        return alertRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    // 읽지 않은 알림 조회
    public List<Alert> getUnreadAlertsForUser(Long userId) {
        return alertRepository.findUnreadAlertsByUserId(userId);
    }

    // 알림 저장/수정
    public Alert save(Alert alert) {
        return alertRepository.save(alert);
    }
}