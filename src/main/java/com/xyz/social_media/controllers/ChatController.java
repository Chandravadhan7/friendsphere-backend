package com.xyz.social_media.controllers;

import com.xyz.social_media.models.ChatMessage;
import com.xyz.social_media.models.Messages;
import com.xyz.social_media.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;

    @Autowired
    public ChatController(SimpMessagingTemplate messagingTemplate, MessageService messageService) {
        this.messagingTemplate = messagingTemplate;
        this.messageService = messageService;
    }

    // Clients send to /app/chat.send
    @MessageMapping("/chat.send")
    public void sendMessage(@Payload ChatMessage chatMessage) {
        // For now, server simply relays the message to subscribers of the conversation
        if (chatMessage.getConversationId() != null) {
            String dest = "/topic/messages/" + chatMessage.getConversationId();
            // Persist the message (store ciphertext as content) before broadcasting
            try {
                Messages msg = new Messages();
                msg.setConversationId(chatMessage.getConversationId());
                msg.setSenderId(chatMessage.getSenderId());
                msg.setMessageType(chatMessage.getType() != null ? chatMessage.getType().name() : "CHAT");
                msg.setContent(chatMessage.getContent());
                msg.setCreatedAt(chatMessage.getCreatedAt() != null ? chatMessage.getCreatedAt() : System.currentTimeMillis());
                msg.setIsDeleted(false);
                Messages saved = messageService.sendMessage(msg);
                // attach persisted id to the outgoing payload so clients can reference it
                if (saved != null) {
                    chatMessage.setMessageId(saved.getMessageId());
                }
            } catch (Exception ex) {
                // ignore persistence error but still relay
            }

            messagingTemplate.convertAndSend(dest, chatMessage);
        }
    }

    // Edit message
    @MessageMapping("/chat.edit")
    public void editMessage(@Payload ChatMessage chatMessage) {
        if (chatMessage.getMessageId() != null && chatMessage.getConversationId() != null) {
            try {
                Messages msg = messageService.getMessageById(chatMessage.getMessageId());
                if (msg != null && msg.getSenderId().equals(chatMessage.getSenderId())) {
                    msg.setContent(chatMessage.getContent());
                    msg.setUpdatedAt(System.currentTimeMillis());
                    messageService.updateMessage(msg);
                    
                    chatMessage.setType(ChatMessage.MessageType.EDIT);
                    chatMessage.setUpdatedAt(msg.getUpdatedAt());
                    
                    String dest = "/topic/messages/" + chatMessage.getConversationId();
                    messagingTemplate.convertAndSend(dest, chatMessage);
                }
            } catch (Exception ex) {
                // error handling
            }
        }
    }

    // Delete message
    @MessageMapping("/chat.delete")
    public void deleteMessage(@Payload ChatMessage chatMessage) {
        if (chatMessage.getMessageId() != null && chatMessage.getConversationId() != null) {
            try {
                Messages msg = messageService.getMessageById(chatMessage.getMessageId());
                if (msg != null && msg.getSenderId().equals(chatMessage.getSenderId())) {
                    msg.setIsDeleted(true);
                    msg.setUpdatedAt(System.currentTimeMillis());
                    messageService.updateMessage(msg);
                    
                    chatMessage.setType(ChatMessage.MessageType.DELETE);
                    chatMessage.setDeleted(true);
                    chatMessage.setUpdatedAt(msg.getUpdatedAt());
                    
                    String dest = "/topic/messages/" + chatMessage.getConversationId();
                    messagingTemplate.convertAndSend(dest, chatMessage);
                }
            } catch (Exception ex) {
                // error handling
            }
        }
    }
}
