package com.portfolio.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_GALLERY_FOLDER",
       uniqueConstraints = @UniqueConstraint(columnNames = {"USER_ID", "FOLDER_NAME"}))
public class GalleryFolder {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GALLERY_FOLDER_SEQ_GEN")
    @SequenceGenerator(name = "GALLERY_FOLDER_SEQ_GEN", sequenceName = "TB_GALLERY_FOLDER_SEQ", allocationSize = 1)
    @Column(name = "FOLDER_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Column(name = "FOLDER_NAME", nullable = false, length = 50)
    private String folderName;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public GalleryFolder() {}

    public GalleryFolder(User user, String folderName) {
        this.user = user;
        this.folderName = folderName;
    }

    // Getter / Setter
    public Long getId() { return id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getFolderName() { return folderName; }
    public void setFolderName(String folderName) { this.folderName = folderName; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
