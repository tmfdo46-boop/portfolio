package com.portfolio.model;

import java.time.LocalDateTime;
import javax.persistence.*;

@Entity
@Table(name = "TB_LIKE", uniqueConstraints = @UniqueConstraint(columnNames = {"USER_ID", "POST_ID"}))
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LIKE_SEQ_GENERATOR")
    @SequenceGenerator(name = "LIKE_SEQ_GENERATOR", sequenceName = "TB_LIKE_SEQ", allocationSize = 1)
    @Column(name = "LIKE_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() { this.createdAt = LocalDateTime.now(); }

    // getter / setter 생략
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Post getPost() { return post; }
    public void setPost(Post post) { this.post = post; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

