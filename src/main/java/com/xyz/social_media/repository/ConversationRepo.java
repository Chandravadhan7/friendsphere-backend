package com.xyz.social_media.repository;

import com.xyz.social_media.models.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface ConversationRepo extends JpaRepository<Conversation,Long> {

    Conversation save(Conversation conversation);

    @Query(value = "select * from conversation c where c.conversation_id= :conversationId",nativeQuery = true)
    Conversation getConversationById(Long conversationId);

    @Query(value = "select c.* from conversation c " +
                   "join conversation_participants cp on c.conversation_id = cp.conversation_id " +
                   "where cp.user_id= :userId " +
                   "and (cp.is_deleted is null or cp.is_deleted = false)",
           nativeQuery = true)
    List<Conversation> getAllConversationByUserId(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query(value = "delete from conversation c where c.conversation_id= :conversationId",nativeQuery = true)
    void deleteById(@Param("conversationId") Long conversationId);
}
