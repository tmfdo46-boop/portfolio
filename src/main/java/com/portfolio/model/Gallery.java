package com.portfolio.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_GALLERY")
public class Gallery {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GALLERY_SEQ_GEN")
    @SequenceGenerator(name = "GALLERY_SEQ_GEN", sequenceName = "TB_GALLERY_SEQ", allocationSize = 1)
    @Column(name = "GALLERY_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Column(name = "IMAGE_URL", nullable = false, length = 200)
    private String imageUrl;

    @Column(name = "FOLDER_ID", nullable = false, length = 50)
    private Long folderId;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // 기본 생성자
    public Gallery() {}

    public Gallery(User user, String imageUrl, Long folderId) {
        this.user = user;
        this.imageUrl = imageUrl;
        this.folderId = folderId;
    }

    // Getter / Setter
    public Long getId() { return id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public Long getFolderId() { return folderId; }
    public void setFolderId(Long folderId) { this.folderId = folderId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
