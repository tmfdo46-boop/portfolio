package com.portfolio.controller;

import com.portfolio.model.Message;
import com.portfolio.model.User;
import com.portfolio.service.MessageService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    // 친구와 대화 조회
    @GetMapping("/chat")
    public List<Message> getChat(@RequestParam Long friendId, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");

        return messageService.getChatMessages(loginUser.getId(), friendId);
    }

    // 메시지 전송
    @PostMapping("/send")
    @ResponseBody
    public void sendMessage(@RequestParam Long receiverId,
                            @RequestParam String content,
                            HttpSession session) {

        User sender = (User) session.getAttribute("loginUser");
        messageService.sendMessage(sender.getId(), receiverId, content);
    }
}
