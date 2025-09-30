package com.user.matching.repository;

import com.user.matching.entity.User;
import com.user.matching.entity.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User,UUID> {
    @Query("UPDATE User u SET u.preferences = :preferences WHERE u.id = :userId")
    public User updatePreferences (UUID userId, UserPreference preferences);

    boolean existsByEmail(String email);

    // Fetch users with preferences to avoid lazy loading issues
    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.preferences")
    List<User> findAllWithPreferences();

    @Query("""
    SELECT u
    FROM User u
    JOIN u.preferences up
    WHERE u.id <> :userId
      AND up.genre IN (
          SELECT p.genre
          FROM UserPreference p
          WHERE p.user.id = :userId
      )
    GROUP BY u
    ORDER BY COUNT(up.genre) DESC
    """)
    List<User> findUsersRankedByGenreMatches(@Param("userId") UUID userId);

}
