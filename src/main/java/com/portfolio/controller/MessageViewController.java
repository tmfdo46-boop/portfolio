package com.portfolio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/messages")
public class MessageViewController {

    // /messages/view 요청 → messages.html 반환
    @GetMapping("/view")
    public String messageView() {
        return "messages"; // resources/templates/messages.html
    }
}
