package com.vaultionizer.vaultserver.helpers;

import com.vaultionizer.vaultserver.model.dto.GetVersionResponseDto;

public class Config {
    public static final int MIN_USERNAME_LENGTH = 4;
    public static final int MIN_USER_KEY_LENGTH = 64;

    public static final GetVersionResponseDto VERSION = new GetVersionResponseDto(
            "Vaultionizer v0.1",
            "No maintainer"
    );

    public static final String WEBSOCKET_PREFIX = "/api/ws";
    public static final String WEBSOCKET_RES = "/api/wsres";
    public static final String WEBSOCKET_DOWNLOAD = WEBSOCKET_RES + "/download/";
    public static final String WEBSOCKET_UPLOAD = WEBSOCKET_PREFIX + "/upload";

    public static final String SPACE_PATH = "/home/vaultionizer/spaces/";
}
