package com.portfolio.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_MESSAGE")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MESSAGE_SEQ_GENERATOR")
    @SequenceGenerator(
            name = "MESSAGE_SEQ_GENERATOR",
            sequenceName = "TB_MESSAGE_SEQ",
            allocationSize = 1
    )
    @Column(name = "MESSAGE_ID")
    private Long id;

    // 발신자
    @ManyToOne
    @JoinColumn(name = "SENDER_ID", nullable = false)
    private User sender;

    // 수신자
    @ManyToOne
    @JoinColumn(name = "RECEIVER_ID", nullable = false)
    private User receiver;

    @Column(name = "CONTENT", nullable = false, length = 500)
    private String content;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // 기본 생성자
    public Message() {}

    // 맞춤 생성자
    public Message(User sender, User receiver, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
    }

    // Getter / Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getSender() { return sender; }
    public void setSender(User sender) { this.sender = sender; }

    public User getReceiver() { return receiver; }
    public void setReceiver(User receiver) { this.receiver = receiver; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
