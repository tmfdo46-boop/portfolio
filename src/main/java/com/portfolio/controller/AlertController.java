package com.portfolio.controller;

import com.portfolio.dto.AlertDto;
import com.portfolio.model.Alert;
import com.portfolio.model.User;
import com.portfolio.service.AlertService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/alerts")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    // 로그인 사용자 알림 조회
    @GetMapping("/list")
    public List<AlertDto> getAlerts(HttpSession session) {
        User user = (User) session.getAttribute("loginUser");
        if(user == null) return List.of();

        List<Alert> alerts = alertService.getAlertsForUser(user.getId());
        return alerts.stream()
                .map(a -> new AlertDto(
                        a.getId(),
                        a.getContent(),
                        a.getCreatedAt(),
                        a.getUser().getNickname(),  // Lazy 로딩 가능, 트랜잭션 안에서
                        a.getReadYn()
                ))
                .collect(Collectors.toList());
    }

    // 알림 읽음 처리
    @PutMapping("/read/{id}")
    public void markAsRead(@PathVariable Long id) {
        Alert alert = alertService.findById(id);
        alert.setReadYn("Y");
        alertService.save(alert);
    }

}
