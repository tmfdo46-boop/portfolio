package com.portfolio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.portfolio.model.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);

    @Query("SELECT COUNT(f) FROM Follow f WHERE f.following.id = :id")
    int countFollowersByUserId(Long id);

    @Query("SELECT COUNT(f) FROM Follow f WHERE f.follower.id = :id")
    int countFollowingByUserId(Long id);

    @Query("SELECT COUNT(p) FROM Post p WHERE p.user.id = :id")
    int countPostsByUserId(Long id);
}
