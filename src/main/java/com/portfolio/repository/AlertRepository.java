package com.portfolio.repository;

import com.portfolio.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
    @Query("SELECT a FROM Alert a JOIN a.user u WHERE u.id = :userId ORDER BY a.createdAt DESC")
    List<Alert> findByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);

    @Query("SELECT a FROM Alert a WHERE a.user.id = :userId AND a.readYn = 'N'")
    List<Alert> findUnreadAlertsByUserId(@Param("userId") Long userId);
}
