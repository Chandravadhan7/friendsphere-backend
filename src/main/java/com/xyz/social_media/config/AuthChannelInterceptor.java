package com.xyz.social_media.config;

import com.xyz.social_media.models.Session;
import com.xyz.social_media.repository.SessionRepo;
import com.xyz.social_media.utilities.UtilityHelper;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

/**
 * Validates STOMP CONNECT frames for presence of sessionId and userId and checks session validity.
 * If validation fails, incoming message is rejected by returning null.
 */
public class AuthChannelInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null) return message;

        // Only check CONNECT frames (and possibly SUBSCRIBE) — allow other frames through
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String sessionId = accessor.getFirstNativeHeader("sessionId");
            String userIdHeader = accessor.getFirstNativeHeader("userId");
            if (sessionId == null || userIdHeader == null) {
                // missing auth headers - reject
                return null;
            }

            Long userId;
            try {
                userId = Long.valueOf(userIdHeader);
            } catch (NumberFormatException e) {
                return null;
            }

            // Validate session using SessionRepo via a lookup — obtain bean lazily to avoid static injection
            try {
                SessionRepo sessionRepo = BeanUtil.getBean(SessionRepo.class);
                Session session = sessionRepo.findBySessionIdAndStatusAndExpiresAtGreaterThan(sessionId, UtilityHelper.getCurrentMillis());
                if (session == null) {
                    return null;
                }
                if (!session.getUserId().equals(userId)) {
                    return null;
                }
            } catch (Exception ex) {
                // If we cannot validate for any reason, reject the connect
                return null;
            }
        }

        return message;
    }
}
