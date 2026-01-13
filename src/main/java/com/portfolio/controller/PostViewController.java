package com.portfolio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/posts")
public class PostViewController {
    // /posts/write 요청 → postWrite.html 반환
    @GetMapping("/write")
    public String postWrite() {
        return "postWrite"; // resources/templates/postWrite.html
    }

    @GetMapping("/detailView")
    public String postDetailView() {
        return "postDetail"; // resources/templates/postDetail.html
    }
}
