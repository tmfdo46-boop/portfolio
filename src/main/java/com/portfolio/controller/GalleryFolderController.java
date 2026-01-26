package com.portfolio.controller;

import com.portfolio.model.GalleryFolder;
import com.portfolio.model.User;
import com.portfolio.service.GalleryFolderService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/gallery")
public class GalleryFolderController {

    private final GalleryFolderService folderService;

    public GalleryFolderController(GalleryFolderService folderService) {
        this.folderService = folderService;
    }

    // 폴더 생성
    @PostMapping("/folder/create")
    public String createFolder(@RequestBody Map<String, String> data, HttpSession session) {
        try {
            User user = (User) session.getAttribute("loginUser");
            String folderName = data.get("folderName");
            folderService.createFolder(user, folderName);
            return "폴더 생성 성공";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    // 사용자 폴더 조회
    @GetMapping("/folders")
    public List<GalleryFolder> getFolders(HttpSession session) {
        User user = (User) session.getAttribute("loginUser");
        return folderService.getFoldersByUser(user);
    }
}
