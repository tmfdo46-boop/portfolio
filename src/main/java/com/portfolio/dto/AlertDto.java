package com.portfolio.dto;

import java.time.LocalDateTime;

public class AlertDto {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private String userNickname; // alert 받는 사람 또는 작성자 닉네임
    private String readYn;

    public AlertDto(Long id, String content, LocalDateTime createdAt, String userNickname, String readYn) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.userNickname = userNickname;
        this.readYn = readYn;
    }

    // Getter / Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public String getUserNickname() { return userNickname; }
    public void setUserNickname(String userNickname) { this.userNickname = userNickname; }
    public String getReadYn() { return readYn; }
    public void setReadYn(String readYn) { this.readYn = readYn; }
}