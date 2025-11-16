package com.xyz.social_media.service;

import com.xyz.social_media.models.ConversationParticipants;
import com.xyz.social_media.models.Messages;
import com.xyz.social_media.repository.ConversationParticipantRepository;
import com.xyz.social_media.repository.MessageRepo;
import com.xyz.social_media.utilities.UtilityHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    private MessageRepo messageRepo;
    private ConversationParticipantRepository conversationParticipantRepository;

    @Autowired
    public MessageService(MessageRepo messageRepo, ConversationParticipantRepository conversationParticipantRepository) {
        this.messageRepo = messageRepo;
        this.conversationParticipantRepository = conversationParticipantRepository;
    }

    public Messages sendMessage(Messages messages){
        messages.setCreatedAt(UtilityHelper.getCurrentMillis());
        return messageRepo.save(messages);
    }

    public Messages getMessageById(Long messageId) {
        return messageRepo.findById(messageId).orElse(null);
    }

    public Messages updateMessage(Messages message) {
        return messageRepo.save(message);
    }

    public List<Messages> getMessagesByConversationId(Long conversationId){
        return messageRepo.getMessagesByConversationId(conversationId);
    }

    public void deleteMessage(Long messageId){
        messageRepo.deleteMessageById(messageId);
    }

    public List<Messages> getLatestUnseenMessage(Long conversationId, Long userId) {
        ConversationParticipants cp = conversationParticipantRepository.findByConversationIdAndUserId(conversationId, userId);
        Long lastSeenAt = cp.getLastSeenAt();
        System.out.println("Last Seen At: " + lastSeenAt);
        return messageRepo.findLatestUnseenMessage(conversationId, userId, lastSeenAt);
    }

    public Messages getLatestMessages(Long conversationId){
        Messages messages = messageRepo.findLastMessageByConversationId(conversationId);
        return messages;
    }

}
