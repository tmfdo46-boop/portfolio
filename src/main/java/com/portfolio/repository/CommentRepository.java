package com.portfolio.repository;

import com.portfolio.model.Comment;
import com.portfolio.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostOrderByCreatedAtAsc(Post post);
    
    @Query("select c from Comment c where c.post.id = :postId")
    List<Comment> findByPostId(@Param("postId") Long postId);
}
