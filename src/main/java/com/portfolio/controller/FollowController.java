package com.portfolio.controller;

import org.springframework.web.bind.annotation.*;

import com.portfolio.service.FollowService;
import com.portfolio.model.User;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/follows")
public class FollowController {

    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    // 내가 팔로우 중인 친구 목록
    @GetMapping("/friends")
    public List<User> getFollowingUsers(HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) throw new RuntimeException("로그인 필요");
        return followService.getFollowingUsers(loginUser.getId());
    }

    // 팔로우 하기
    @PostMapping("/status/{userId}")
    public void follow(@PathVariable Long userId, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) throw new RuntimeException("로그인 필요");
        followService.follow(loginUser.getId(), userId);
    }
}
