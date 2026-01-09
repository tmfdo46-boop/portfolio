package com.portfolio.dto;

import com.portfolio.model.Post;
import com.portfolio.model.PostImage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PostDto {
    private Long id;
    private String content;
    private String authorName;
    private LocalDateTime createdAt;
    private int likeCount;
    private List<String> imagePaths = new ArrayList<>();

    // 생성자
    public PostDto(Post post) {
        this.id = post.getId();
        this.content = post.getContent();
        this.authorName = post.getUser().getName();
        this.createdAt = post.getCreatedAt();
        this.likeCount = post.getLikeCount();

        if (post.getImages() != null) {
            this.imagePaths = post.getImages().stream()
                                  .map(PostImage::getImagePath)
                                  .collect(Collectors.toList());
        }
    }

    // getter / setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public List<String> getImagePaths() { return imagePaths; }
    public void setImagePaths(List<String> imagePaths) { this.imagePaths = imagePaths; }
    public int getLikeCount() { return likeCount; }
    public void setLikeCount(int likeCount) { this.likeCount = likeCount; }
}
