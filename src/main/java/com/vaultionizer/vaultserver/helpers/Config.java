package com.vaultionizer.vaultserver.helpers;

import com.vaultionizer.vaultserver.model.dto.GetVersionResponseDto;

public class Config {
    public static final int MIN_USERNAME_LENGTH = 4;
    public static final int MIN_USER_KEY_LENGTH = 60;
    public static final int MAX_SESSION_AGE = 1200;
    public static final int MAX_UPLOAD_AGE = 86400;


    public static final GetVersionResponseDto VERSION = new GetVersionResponseDto(
            "Vaultionizer v0.1",
            "No maintainer",
            false
    );

    public static final String serverUser = "root";
    public static final String serverAuth = "toor";

    public static final String WEBSOCKET_PREFIX = "/api/ws";
    public static final String WEBSOCKET_RES = "/api/wsres";
    public static final String WEBSOCKET_DOWNLOAD = WEBSOCKET_RES + "/download/";
    public static final String WEBSOCKET_UPLOAD = WEBSOCKET_PREFIX + "/upload";

    public static final String SPACE_PATH = "/home/vaultionizer/spaces/";



    public static final int SESSION_JOB_DELAY = 3600 * 1000;
    public static final int PENDING_UPLOAD_JOB_DELAY = 86400 * 1000;

    public static final String randomAlgo = "SHA1PRNG";
}
