package com.portfolio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserViewController {

    // 회원가입 화면
    @GetMapping("/register")
    public String registerForm() {
        return "register"; // templates/register.html
    }

    // 로그인 화면
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    // 프로필 페이지
    @GetMapping("/profilePage")
    public String profilePage() {
        return "profile"; // templates/profile.html
    }

    // 프로필 편집 화면
    @GetMapping("/profile/edit")
    public String editProfile() {
        return "profileEdit";
    }
}