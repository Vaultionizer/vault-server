package com.vaultionizer.vaultserver.controllers;


import com.vaultionizer.vaultserver.helpers.Config;
import com.vaultionizer.vaultserver.model.dto.WebsocketFileDto;
import com.vaultionizer.vaultserver.model.dto.wserrors.GenericWSError;
import com.vaultionizer.vaultserver.model.dto.wserrors.UploadData;
import com.vaultionizer.vaultserver.model.dto.wserrors.WS_ERROR;
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
        String wsToken = nativeHeaders.getFirst("websocketToken");

        if (sessionKey == null || wsToken == null) return;

        Long sessID = sessionService.getSessionID(userID, sessionKey);
        if (sessID == -1) {
            return;
        }

        if (userID == null || spaceID == null || saveIndex == null) {
            sendError(wsToken, new GenericWSError(WS_ERROR.MISSHAPEN_UPLOAD,
                    new UploadData(userID, spaceID, saveIndex, sessionKey)
            ));
            return;
        }

        int granted = pendingUploadService.uploadFile(spaceID, sessID, saveIndex);
        if (granted == 0) {
            sendError(wsToken, new GenericWSError(WS_ERROR.UPLOAD_NOT_GRANTED,
                    new UploadData(userID, spaceID, saveIndex, sessionKey)
            ));
            return;
        }

        boolean success;
        if (granted == 1){
            // usual upload
            fileService.setUploadFile(spaceID, saveIndex);
            success = fileService.writeToFile(content.getContent(), spaceID, saveIndex);
        }
        else{
            // updating requested
            success = fileService.tryUpdating(content.getContent(), spaceID, saveIndex);
        }

        if (!success) {
            sendError(wsToken, new GenericWSError(WS_ERROR.UPLOAD_UNSUCCESSFUL,
                    new UploadData(userID, spaceID, saveIndex, sessionKey)));
        }
    }

    public synchronized void download(String websocketToken, Long spaceID, Long saveIndex){
        // TODO: check how to set headers (namely: spaceID and saveIndex)
        simpMessagingTemplate.convertAndSend( Config.WEBSOCKET_DOWNLOAD + websocketToken,
                fileService.makeDownload(spaceID, saveIndex));
    }

    public void sendError(String websocketToken, GenericWSError error){
        simpMessagingTemplate.convertAndSend( Config.WEBSOCKET_ERROR + websocketToken, error);
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
