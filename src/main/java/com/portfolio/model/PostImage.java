package com.portfolio.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_POST_IMAGE")
public class PostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POST_IMAGE_SEQ_GEN")
    @SequenceGenerator(
            name = "POST_IMAGE_SEQ_GEN",
            sequenceName = "TB_POST_IMAGE_SEQ",
            allocationSize = 1
    )
    @Column(name = "POST_IMAGE_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID", nullable = false)
    private Post post;

    @Column(name = "IMAGE_PATH", nullable = false)
    private String imagePath;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // 기본 생성자
    public PostImage() {}

    // 생성자
    public PostImage(Post post, String imagePath) {
        this.post = post;
        this.imagePath = imagePath;
    }

    // getter / setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Post getPost() { return post; }
    public void setPost(Post post) { this.post = post; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
