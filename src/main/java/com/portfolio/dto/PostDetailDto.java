package com.portfolio.dto;

import com.portfolio.model.Post;
import com.portfolio.model.PostImage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PostDetailDto {
    private Long id;
    private String content;
    private String nickname;
    private int likeCount;
    private LocalDateTime createdAt;
    private List<String> imageUrls;

    public PostDetailDto(Post post) {
        this.id = post.getId();
        this.content = post.getContent();
        this.nickname = post.getUser().getNickname();
        this.likeCount = post.getLikeCount();
        this.createdAt = post.getCreatedAt();

        // 이미지 있는 경우만
        if (post.getImages() != null) {
            this.imageUrls = post.getImages().stream()
                                  .map(PostImage::getImagePath)
                                  .collect(Collectors.toList());
        }
    }

    // getter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public int getLikeCount() { return likeCount; }
    public void setLikeCount(int likeCount) { this.likeCount = likeCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public List<String> getImageUrls() { return imageUrls; }
    public void setImageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; }
}