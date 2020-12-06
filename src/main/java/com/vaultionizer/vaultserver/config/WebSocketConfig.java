package com.vaultionizer.vaultserver.config;

import com.vaultionizer.vaultserver.helpers.Config;
import com.vaultionizer.vaultserver.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

import static com.vaultionizer.vaultserver.helpers.Config.WEBSOCKET_PREFIX;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final SessionService sessionService;

    @Autowired
    public WebSocketConfig(SessionService sessionService) {
        this.sessionService = sessionService;
    }


    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker(Config.WEBSOCKET_RES);
        config.setApplicationDestinationPrefixes(WEBSOCKET_PREFIX);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(Config.WEBSOCKET_CONNECT).withSockJS();
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.setMessageSizeLimit(Config.MSG_SIZE_LIMITS);
        registration.setSendBufferSizeLimit(Config.MSG_SIZE_LIMITS);
    }


    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new WebSocketChannelFilter(sessionService));
    }
}
