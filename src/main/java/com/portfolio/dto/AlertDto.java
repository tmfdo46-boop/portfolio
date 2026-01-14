package com.portfolio.dto;

import java.time.LocalDateTime;

public class AlertDto {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private Long userId;
    private String readYn;

    public AlertDto(Long id, String content, LocalDateTime createdAt, Long userId, String readYn) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.userId = userId;
        this.readYn = readYn;
    }

    // Getter / Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getReadYn() { return readYn; }
    public void setReadYn(String readYn) { this.readYn = readYn; }
}