package com.portfolio.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.model.Gallery;
import com.portfolio.model.User;
import com.portfolio.service.GalleryService;

@RestController
@RequestMapping("/gallery")
public class GalleryController {

    private final GalleryService galleryService;

    public GalleryController(GalleryService galleryService) {
        this.galleryService = galleryService;
    }

    // 사용자 갤러리 조회
    @GetMapping("/list")
    public Object getGallery(HttpSession session) {
        User user = (User) session.getAttribute("loginUser");
        return galleryService.getUserGallery(user);
    }

    // 폴더별 조회
    @GetMapping("/list/{folderId}")
    public List<Gallery> getGalleryByFolder(@PathVariable("folderId") Long folderId, HttpSession session) {
        User user = (User) session.getAttribute("loginUser");
        if (user == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }
        
        return galleryService.getImagesByFolder(user, folderId);
    }

    // 이미지 저장
    @PostMapping("/save")
    public String saveImage(@RequestBody Map<String, String> data, HttpSession session) {
        User user = (User) session.getAttribute("loginUser");
        if (user == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }

        Long folderId = Long.valueOf(data.get("folderId"));
        String imageUrl = data.get("imageUrl");

        String result = galleryService.saveImage(user, folderId, imageUrl);
        return result;
    }
}
