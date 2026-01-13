package com.portfolio.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_ALERT")
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ALERT_SEQ_GENERATOR")
    @SequenceGenerator(
            name = "ALERT_SEQ_GENERATOR",
            sequenceName = "TB_ALERT_SEQ",
            allocationSize = 1
    )
    @Column(name = "ALERT_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user; // 알림 받는 사용자

    @Column(name = "CONTENT", nullable = false, length = 500)
    private String content;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "READ_YN", nullable = false, length = 1)
    private String readYn = "N"; // 기본값 N

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public Alert() {}

    public Alert(User user, String content) {
        this.user = user;
        this.content = content;
    }

    // Getter / Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public String getReadYn() { return readYn; }
    public void setReadYn(String readYn) { this.readYn = readYn; }
}
