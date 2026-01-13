package com.portfolio.controller;

import com.portfolio.model.Message;
import com.portfolio.model.User;
import com.portfolio.service.MessageService;

import org.springframework.http.ResponseEntity;
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

    // 로그인 사용자 기준 모든 메시지 조회
    @GetMapping("/list")
    public List<Message> getMessages(HttpSession session) {
        User user = (User) session.getAttribute("loginUser");
        return messageService.getMessagesForUser(user);
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

    // 메시지 읽음 처리
    @PutMapping("/read/{id}")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("loginUser");
        messageService.markAsRead(user.getId(), id);
        return ResponseEntity.ok().build();
    }
}
