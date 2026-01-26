package com.portfolio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portfolio.model.Gallery;
import com.portfolio.model.User;

@Repository
public interface GalleryRepository extends JpaRepository<Gallery, Long> {
    List<Gallery> findByUser(User user); // 특정 사용자의 갤러리 조회
    List<Gallery> findByUserAndFolderIdOrderByCreatedAtDesc(User user, Long folderId); // 폴더별 조회
    // 중복 저장 방지
    boolean existsByUserAndImageUrlAndFolderId(User user, String imageUrl, Long folderId);
}
