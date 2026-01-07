package com.portfolio.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.portfolio.model.User;
import com.portfolio.service.PostService;

@Controller
public class MainController {

    @GetMapping("/main")
    public String mainPage() {
        return "main";
    }

    private final PostService postService;

    public MainController(PostService postService) {
        this.postService = postService;
    }

    // 글쓰기 처리
    @PostMapping("/post")
    public String createPost(@RequestParam String content, @SessionAttribute("user") User user) {
        // postService.createPost(user, content);
        return "redirect:/main";
    }

    // 세션 체크 API
    @ResponseBody
    @GetMapping("/api/session")
    public Object sessionCheck(@SessionAttribute(value="user", required=false) User user) {
        if (user != null) {
            return Map.of("loggedIn", true, "nickname", user.getNickname());
        } else {
            return Map.of("loggedIn", false);
        }
    }

    @GetMapping("/message")
    public String message() { return "message"; }
    @GetMapping("/alarm")
    public String alarm() { return "alarm"; }
    @GetMapping("/mypage")
    public String mypage() { return "mypage"; }
}
