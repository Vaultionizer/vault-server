package com.vaultionizer.vaultserver.config;

import com.vaultionizer.vaultserver.model.dto.FileUploadWebsocketDto;
import com.vaultionizer.vaultserver.model.dto.GenericAuthDto;
import com.vaultionizer.vaultserver.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

import java.util.Objects;

import static com.vaultionizer.vaultserver.helpers.Config.WEBSOCKET_PREFIX;

public class WebSocketChannelFilter implements ChannelInterceptor {
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())){
            // TODO: check whether user has rights to subscribe
        }

        return message;
    }
}
