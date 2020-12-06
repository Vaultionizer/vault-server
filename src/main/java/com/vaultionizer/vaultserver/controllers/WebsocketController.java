package com.vaultionizer.vaultserver.controllers;

import com.vaultionizer.vaultserver.helpers.Config;
import com.vaultionizer.vaultserver.model.dto.WebsocketFileDto;
import com.vaultionizer.vaultserver.service.FileService;
import com.vaultionizer.vaultserver.service.PendingUploadService;
import com.vaultionizer.vaultserver.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(maxAge = 3600)
@Controller
public class WebsocketController {
    private final SessionService sessionService;
    private final PendingUploadService pendingUploadService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final FileService fileService;

    @Autowired
    public WebsocketController(SessionService sessionService, PendingUploadService pendingUploadService,
                               SimpMessagingTemplate simpMessagingTemplate, FileService fileService) {
        this.sessionService = sessionService;
        this.pendingUploadService = pendingUploadService;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.fileService = fileService;
    }

    @MessageMapping("/upload")
    public void upload(@Payload WebsocketFileDto content, Message<?> file){
        // TODO: check how to send errors
        if (content == null || content.getContent() == null) return;
        LinkedMultiValueMap<String, String> nativeHeaders = parseNativeHeaders(file.getHeaders().get("nativeHeaders"));
        if (nativeHeaders == null) return;

        // parse headers
        Long userID = parseLongFromHeader(nativeHeaders, "userID");
        Long spaceID = parseLongFromHeader(nativeHeaders, "spaceID");
        Long saveIndex = parseLongFromHeader(nativeHeaders, "saveIndex");
        String sessionKey = nativeHeaders.getFirst("sessionKey");

        if (userID == null || spaceID == null || saveIndex == null || sessionKey == null) return;
        Long sessID = sessionService.getSessionID(userID, sessionKey);
        if (sessID == -1) return;
        boolean granted = pendingUploadService.uploadFile(spaceID, sessID, saveIndex);
        if (!granted) return;

        fileService.setUploadFile(spaceID, saveIndex);

        boolean success = fileService.writeToFile(content.getContent(), spaceID, saveIndex);
        if (!success) { reportError(userID, sessionKey, 500); }
    }

    private void reportError(Long userID, String sessionKey, int status){
        System.out.println("Error");
    }

    public synchronized void download(String websocketToken, Long spaceID, Long saveIndex){
        // TODO: check how to set headers (namely: spaceID and saveIndex)
        simpMessagingTemplate.convertAndSend( Config.WEBSOCKET_DOWNLOAD + websocketToken,
                fileService.makeDownload(spaceID, saveIndex));
    }

    private Long parseLongFromHeader(LinkedMultiValueMap<String, String> map, String key){
        if (map.getFirst(key) == null) return null;
        return Long.parseLong(map.getFirst(key));
    }

    private LinkedMultiValueMap<String, String> parseNativeHeaders(Object o){
        if (o == null) return null;
        if (o instanceof LinkedMultiValueMap){
            return (LinkedMultiValueMap)o;
        }
        return null;
    }
}
