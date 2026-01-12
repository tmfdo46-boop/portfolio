package com.portfolio.repository;

import com.portfolio.model.Follow;
import com.portfolio.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    
    @Query("SELECT f FROM Follow f WHERE f.follower.id = :followerId")
    List<Follow> findByFollowerId(@Param("followerId") Long followerId);

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END " +
           "FROM Follow f " +
           "WHERE f.follower.id = :followerId AND f.following.id = :followingId")
    boolean existsByFollowerIdAndFollowingId(@Param("followerId") Long followerId,
                                            @Param("followingId") Long followingId);

    List<Follow> findByFollower(User follower);
}
