package com.portfolio.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.portfolio.model.Gallery;
import com.portfolio.model.User;
import com.portfolio.repository.GalleryFolderRepository;
import com.portfolio.repository.GalleryRepository;

@Service
public class GalleryService {

    private final GalleryRepository galleryRepository;
    private final GalleryFolderRepository folderRepository;

    public GalleryService(GalleryRepository galleryRepository, GalleryFolderRepository folderRepository) {
        this.galleryRepository = galleryRepository;
        this.folderRepository = folderRepository;
    }

    public List<Gallery> getUserGallery(User user) {
        return galleryRepository.findByUser(user);
    }

    public List<Gallery> getImagesByFolder(User user, Long folderId) {
        return galleryRepository.findByUserAndFolderIdOrderByCreatedAtDesc(user, folderId);
    }

    public String saveImage(User user, Long folderId, String imageUrl) {
        boolean folderExists = folderRepository.existsById(folderId);
        if (!folderExists) {
            //throw new RuntimeException("존재하지 않는 폴더입니다.");
            return "folderAlready";
        }

        // 중복 저장 방지
        boolean exists = galleryRepository
                .existsByUserAndImageUrlAndFolderId(user, imageUrl, folderId);

        if (exists) {
            // throw new RuntimeException("이미 해당 폴더에 저장된 이미지입니다.");
            return "Already";
        }else{
            Gallery gallery = new Gallery(user, imageUrl, folderId);
            galleryRepository.save(gallery);
            return "Success";
        }
    }
}
