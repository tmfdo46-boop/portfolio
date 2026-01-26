package com.portfolio.service;

import com.portfolio.model.GalleryFolder;
import com.portfolio.model.User;
import com.portfolio.repository.GalleryFolderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GalleryFolderService {

    private final GalleryFolderRepository folderRepository;

    public GalleryFolderService(GalleryFolderRepository folderRepository) {
        this.folderRepository = folderRepository;
    }

    // 폴더 생성
    public GalleryFolder createFolder(User user, String folderName) throws Exception {
        // 중복 확인
        GalleryFolder existing = folderRepository.findByUserAndFolderName(user, folderName);
        if (existing != null) {
            throw new Exception("이미 같은 이름의 폴더가 존재합니다.");
        }

        GalleryFolder folder = new GalleryFolder(user, folderName);
        return folderRepository.save(folder);
    }

    // 사용자 폴더 전체 조회
    public List<GalleryFolder> getFoldersByUser(User user) {
        return folderRepository.findByUser(user);
    }
}
