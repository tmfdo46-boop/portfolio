package com.portfolio.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.portfolio.model.User;
import com.portfolio.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ----------------------------
    // 회원가입 화면
    // ----------------------------
    @GetMapping("/register")
    public String registerForm() {
        return "register"; // templates/register.html
    }

    // ----------------------------
    // AJAX 회원가입 처리
    // ----------------------------
    @PostMapping("/register")
    @ResponseBody
    public Map<String, Object> register(@RequestBody Map<String, String> formData) {
        String email = formData.get("email");
        String password = formData.get("password");
        String name = formData.get("name");
        String nickname = formData.get("nickname");
        String hp = formData.get("hp");
        String address = formData.get("address");

        Map<String, Object> res = new HashMap<>();

        if (userService.existsByEmail(email)) {
            res.put("status", "error");
            res.put("message", "이미 사용 중인 이메일입니다.");
            return res;
        }

        User user = new User(email, name, password, nickname, hp, address);
        user.setNickname(email);
        user.setHp(name);
        user.setAddress(password);
        user.setNickname(nickname);
        user.setHp(hp);
        user.setAddress(address);

        userService.saveUser(user);

        res.put("status", "success");
        res.put("message", "회원가입이 완료되었습니다!");
        return res;
    }

    // ----------------------------
    // 로그인 화면
    // ----------------------------
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    // ----------------------------
    // 로그인 처리
    // ----------------------------
    @PostMapping("/login")
    @ResponseBody
    public String login(@RequestBody Map<String, String> loginData, HttpSession session) {
        String email = loginData.get("email");
        String password = loginData.get("password");

        User user = userService.login(email, password);
        if(user != null) {
            session.setAttribute("loginUser", user); // 세션에 사용자 정보 저장
            return "success";
        } else {
            return "fail";
        }
    }

    // ----------------------------
    // 로그아웃
    // ----------------------------
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/users/login";
    }

    // ----------------------------
    // 이메일 중복 체크
    // ----------------------------
    @ResponseBody
    @GetMapping("/check-email")
    public Map<String, Boolean> checkEmail(@RequestParam String email) {
        boolean exists = userService.existsByEmail(email);
        return Map.of("exists", exists);
    }
}
