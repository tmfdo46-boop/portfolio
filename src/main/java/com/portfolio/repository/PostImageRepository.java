package com.portfolio.repository;

import com.portfolio.model.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostImageRepository extends JpaRepository<PostImage, Long> {

    // 특정 게시글의 이미지 전체 조회
    List<PostImage> findByPostId(Long postId);
}
