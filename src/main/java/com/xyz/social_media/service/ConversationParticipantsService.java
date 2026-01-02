package com.xyz.social_media.service;

import com.xyz.social_media.models.ConversationParticipants;
import com.xyz.social_media.repository.ConversationParticipantRepository;
import com.xyz.social_media.utilities.UtilityHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConversationParticipantsService {
    private ConversationParticipantRepository conversationParticipantRepository;

    @Autowired
    public ConversationParticipantsService(ConversationParticipantRepository conversationParticipantRepository) {
        this.conversationParticipantRepository = conversationParticipantRepository;
    }

    public ConversationParticipants addParticipant(ConversationParticipants conversationParticipants){
        return conversationParticipantRepository.save(conversationParticipants);
    }

    public List<ConversationParticipants> getParticipantsOfConversation(Long conversationId){
        return conversationParticipantRepository.getParticipantsByConversationId(conversationId);
    }

    public ConversationParticipants setLastSeen(Long conversationId, Long userId) {
        ConversationParticipants conversationParticipants =
                conversationParticipantRepository.findByConversationIdAndUserId(conversationId, userId);

        if (conversationParticipants == null) {
            throw new RuntimeException("No conversation participant found for conversationId: " + conversationId + " and userId: " + userId);
        }

        conversationParticipants.setLastSeenAt(UtilityHelper.getCurrentMillis());
        return conversationParticipantRepository.save(conversationParticipants);
    }

    public void removeParticipant(Long conversationId, Long userId) {
        ConversationParticipants conversationParticipants =
                conversationParticipantRepository.findByConversationIdAndUserId(conversationId, userId);

        if (conversationParticipants == null) {
            throw new RuntimeException("No conversation participant found for conversationId: " + conversationId + " and userId: " + userId);
        }

        conversationParticipantRepository.delete(conversationParticipants);
    }

}
