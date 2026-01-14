package com.portfolio.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portfolio.model.Like;
import com.portfolio.model.Post;
import com.portfolio.model.User;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByUserAndPost(User user, Post post);

    Optional<Like> findByUserIdAndPostId(Long userId, Long postId);
}
