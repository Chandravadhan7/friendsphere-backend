package com.xyz.social_media.service;

import com.xyz.social_media.models.Conversation;
import com.xyz.social_media.models.ConversationParticipants;
import com.xyz.social_media.repository.ConversationParticipantRepository;
import com.xyz.social_media.repository.ConversationRepo;
import com.xyz.social_media.repository.MessageRepo;
import com.xyz.social_media.utilities.UtilityHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConversationService {
    private ConversationRepo conversationRepo;
    private ConversationParticipantRepository conversationParticipantRepository;
    private MessageRepo messageRepo;

    @Autowired
    public ConversationService(ConversationRepo conversationRepo,
                              ConversationParticipantRepository conversationParticipantRepository,
                              MessageRepo messageRepo) {
        this.conversationRepo = conversationRepo;
        this.conversationParticipantRepository = conversationParticipantRepository;
        this.messageRepo = messageRepo;
    }

    public Conversation createConversation(Conversation conversation){
        conversation.setCreatedAt(UtilityHelper.getCurrentMillis());
        return conversationRepo.save(conversation);
    }

    public Conversation getConversationById(Long conversationId){
        return conversationRepo.getConversationById(conversationId);
    }

    public List<Conversation> getAllConversationsOfUser(Long userId){
        List<Conversation> conversations = conversationRepo.getAllConversationByUserId(userId);
        return conversations;
    }
    public Conversation updateConversation(Long conversationId,Conversation conversation){
        Conversation existing = getConversationById(conversationId);
        existing.setTitle(conversation.getTitle());
        existing.setUpdatedAt(UtilityHelper.getCurrentMillis());
        return conversationRepo.save(existing);
    }

    public void deleteConversation(Long conversationId){
        // Delete all messages in the conversation first
        messageRepo.deleteMessagesByConversationId(conversationId);
        
        // Delete all participants
        conversationParticipantRepository.deleteByConversationId(conversationId);
        
        // Finally delete the conversation
        conversationRepo.deleteById(conversationId);
    }

    public void deleteConversationForUser(Long conversationId, Long userId){
        // Soft delete: Mark conversation as deleted for this user only
        ConversationParticipants participant = conversationParticipantRepository.findByConversationIdAndUserId(conversationId, userId);
        if (participant != null) {
            participant.setIsDeleted(true);
            participant.setDeletedAt(UtilityHelper.getCurrentMillis());
            conversationParticipantRepository.save(participant);
        }
    }

    public void restoreConversationForUser(Long conversationId, Long userId){
        // Restore: Mark conversation as not deleted for this user
        ConversationParticipants participant = conversationParticipantRepository.findByConversationIdAndUserId(conversationId, userId);
        if (participant != null) {
            participant.setIsDeleted(false);
            participant.setDeletedAt(null);
            conversationParticipantRepository.save(participant);
        }
    }
}
