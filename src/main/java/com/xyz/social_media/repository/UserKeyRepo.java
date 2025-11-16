package com.xyz.social_media.repository;

import com.xyz.social_media.models.UserKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserKeyRepo extends JpaRepository<UserKey, Long> {
    UserKey findByUserId(Long userId);
}
