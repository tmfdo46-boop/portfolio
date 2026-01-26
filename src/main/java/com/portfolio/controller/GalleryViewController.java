package com.portfolio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/gallery")
public class GalleryViewController {
    // 프로필 갤러리 화면
    @GetMapping("/view")
    public String viewGallery() {
        return "gallery";
    }
}