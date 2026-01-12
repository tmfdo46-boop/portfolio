package com.portfolio.repository;

import com.portfolio.model.Message;
import com.portfolio.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    // 로그인 사용자가 보낸/받은 메시지 모두 조회 (최신 순)
    List<Message> findBySenderOrReceiverOrderByCreatedAtDesc(User sender, User receiver);
    
    @Query("SELECT m FROM Message m " +
        "WHERE (m.sender.id = :loginUserId AND m.receiver.id = :friendId) " +
        "   OR (m.sender.id = :friendId AND m.receiver.id = :loginUserId) " +
        "ORDER BY m.createdAt ASC")
    List<Message> findChatBetween(
        @Param("loginUserId") Long loginUserId,
        @Param("friendId") Long friendId
    );

}
