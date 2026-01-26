package com.portfolio.repository;

import com.portfolio.model.GalleryFolder;
import com.portfolio.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GalleryFolderRepository extends JpaRepository<GalleryFolder, Long> {
    List<GalleryFolder> findByUser(User user); // 사용자별 폴더 목록 조회
    GalleryFolder findByUserAndFolderName(User user, String folderName); // 같은 이름 폴더 조회 (중복 방지)
}
