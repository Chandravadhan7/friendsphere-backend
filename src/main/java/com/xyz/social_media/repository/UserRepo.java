package com.xyz.social_media.repository;

import com.xyz.social_media.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepo extends JpaRepository<User,Long> {

    User save(User user);

    @Query(value = "select * from user u where u.email = :email",nativeQuery = true)
    User getUserByEmail(@Param("email") String email);

    @Query(value = "select * from user u where u.id = :userId",nativeQuery = true)
    User getUserByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT u.id FROM user u WHERE u.id <> :userId AND u.id NOT IN " +
            "(SELECT f.user_id2 FROM friends f WHERE f.user_id1 = :userId and f.status = 'accept'" +
            " UNION " +
            "SELECT f.user_id1 FROM friends f WHERE f.user_id2 = :userId and f.status = 'accept')",nativeQuery = true)
    List<Long> findSuggestedFriendIds(Long userId);

}
