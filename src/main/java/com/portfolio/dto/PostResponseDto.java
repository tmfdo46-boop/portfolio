package com.portfolio.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.portfolio.model.Post;
import com.portfolio.model.PostImage;

public class PostResponseDto {

    private Long id;
    private String content;
    private String nickname;
    private LocalDateTime createdAt;
    private List<String> imageUrls;
    private int likeCount;           // ✅ 반드시 추가

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.content = post.getContent();
        this.nickname = post.getUser().getNickname();
        this.createdAt = post.getCreatedAt();

        // 이미지 리스트 변환
        this.imageUrls = post.getImages()
                            .stream()
                            .map(PostImage::getImagePath)
                            .toList();
        this.likeCount = post.getLikeCount();
    }
    
    // getter / setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public List<String> getImageUrls() { return imageUrls; }
    public void setImageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; }
    public int getLikeCount() { return likeCount; }
    public void setLikeCount(int likeCount) { this.likeCount = likeCount; }
}
