package com.portfolio.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.portfolio.model.Post;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc();
}