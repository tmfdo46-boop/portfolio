package com.portfolio.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.portfolio.model.Post;
import com.portfolio.model.PostImage;

public class PostResponseDto {
    private Long id;
    private Long userId;
    private String content;
    private String nickname;
    private LocalDateTime createdAt;
    private List<String> imageUrls;
    private int likeCount;
    private int commentCount;
    private String profileImage;
    private boolean following;
    private boolean likedByMe;
    private String result;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.userId = post.getUser().getId();
        this.content = post.getContent();
        this.nickname = post.getUser().getNickname();
        this.createdAt = post.getCreatedAt();
        this.result = post.getResult();
        
        if(post.getComments() != null) {
            this.commentCount = post.getComments().size();
        } else {
            this.commentCount = 0;
        }

        String userImage = post.getUser().getProfileImage();
        if(userImage == null || userImage.isEmpty()) {
            this.profileImage = "/images/default_profile.png";
        } else {
            this.profileImage = userImage;
        }

        this.imageUrls = post.getImages()
                            .stream()
                            .map(PostImage::getImagePath)
                            .toList();

        this.likeCount = post.getLikeCount();
    }

    public PostResponseDto(Post post, boolean isFollowing, Long loginUserId) {
        this(post);
        this.following = isFollowing;
        this.likedByMe = post.getLikes().stream()
                             .anyMatch(like -> like.getUser().getId().equals(loginUserId));
    }

    // getter / setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
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
    public int getCommentCount() { return commentCount; }
    public void setCommentCount(int commentCount) { this.commentCount = commentCount; }
    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }
    public boolean isFollowing() { return following; }
    public void setFollowing(boolean following) { this.following = following; }
    public boolean isLikedByMe() { return likedByMe; }
    public void setLikedByMe(boolean likedByMe) { this.likedByMe = likedByMe; }
    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
}
