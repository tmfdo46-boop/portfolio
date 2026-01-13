package com.portfolio.service;

import com.portfolio.model.Message;
import com.portfolio.model.User;
import com.portfolio.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.transaction.Transactional;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;

    public MessageService(MessageRepository messageRepository, UserService userService) {
        this.messageRepository = messageRepository;
        this.userService = userService;
    }

    // 로그인 사용자 기준 모든 메시지 조회
    public List<Message> getMessagesForUser(User user) {
        return messageRepository.findBySenderOrReceiverOrderByCreatedAtDesc(user, user);
    }

    // 로그인 사용자 + 친구 대화 조회
    public List<Message> getChatMessages(Long loginUser, Long friend) {
        return messageRepository.findChatBetween(loginUser, friend);
    }

    // 메시지 전송
    @Transactional
    public void sendMessage(Long senderId, Long receiverId, String content) {
        User sender = userService.findById(senderId);
        User receiver = userService.findById(receiverId);

        Message message = new Message(sender, receiver, content);
        messageRepository.save(message);
    }

    // 메시지 읽음 처리
    @Transactional
    public void markAsRead(Long userId, Long senderId) {
        List<Message> messages = messageRepository.findBySenderIdAndReceiverId(senderId, userId);
        for (Message msg : messages) {
            msg.setReadYn("Y");
        }
    }
}
