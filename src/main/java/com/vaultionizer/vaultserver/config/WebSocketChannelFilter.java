package com.vaultionizer.vaultserver.config;

import com.vaultionizer.vaultserver.service.SessionService;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

import static com.vaultionizer.vaultserver.helpers.Config.WEBSOCKET_DOWNLOAD;
import static com.vaultionizer.vaultserver.helpers.Config.WEBSOCKET_UPLOAD;

public class WebSocketChannelFilter implements ChannelInterceptor {
    private final SessionService sessionService;

    public WebSocketChannelFilter(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())){
            // TODO: check whether user has rights to subscribe
            String dest = accessor.getDestination();
            if (dest == null) return null;
            if (!dest.startsWith(WEBSOCKET_DOWNLOAD)){
                return null; // TODO: send error
            }
            String websocketToken = dest.substring(WEBSOCKET_DOWNLOAD.length());
            String sessionKey = accessor.getFirstNativeHeader("sessionKey");
            String userID = accessor.getFirstNativeHeader("userID");
            if (userID == null || sessionKey == null || websocketToken.length() == 0 || userID.length() == 0) return null;
            if (!sessionService.checkValidWebsocketToken(Long.parseLong(userID), websocketToken, sessionKey)) {
                return null;
            }
            // subscription is valid
        }

        else if (StompCommand.SEND.equals(accessor.getCommand())){
            // check whether to upload endpoint (only one that is legitimate)
            String dest = accessor.getDestination();
            if (dest == null) return null;
            if (!dest.startsWith(WEBSOCKET_UPLOAD)){
                return null; // TODO: send error
            }
        }

        return message;
    }
}
