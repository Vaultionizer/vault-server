package com.vaultionizer.vaultserver.helpers;

import com.vaultionizer.vaultserver.model.dto.GetVersionResponseDto;

public class Config {
    public static final int MIN_USERNAME_LENGTH = 4;
    public static final int MIN_USER_KEY_LENGTH = 60;
    public static final int MAX_SESSION_AGE = 1200;
    public static final int MAX_UPLOAD_AGE = 86400;


    public static final GetVersionResponseDto VERSION = new GetVersionResponseDto(
            "Vaultionizer v1.0",
            "No maintainer",
            false
    );

    public static final String SERVER_USER = "root";
    public static final String SERVER_AUTH = "toor";

    public static final int MSG_SIZE_LIMITS = 50 * 1024 * 1024;
    public static final String WEBSOCKET_CONNECT = "/wss";
    public static final String WEBSOCKET_PREFIX = "/api/ws";
    public static final String WEBSOCKET_RES = "/api/wsres";
    public static final String WEBSOCKET_DOWNLOAD = WEBSOCKET_RES + "/download/";
    public static final String WEBSOCKET_ERROR    = WEBSOCKET_RES + "/error/";
    public static final String WEBSOCKET_UPLOAD = WEBSOCKET_PREFIX + "/upload";

    // is adjusted in the tests (thus not final)
    public static String SPACE_PATH = "/home/vaultionizer/spaces/";



    public static final int SESSION_JOB_DELAY = 3600 * 1000;
    public static final int PENDING_UPLOAD_JOB_DELAY = 86400 * 1000;

    public static final String RANDOM_ALGO = "SHA1PRNG";


    // Realtime stream config
    public static final boolean REALTIME_DISABLED = false; // disable realtime (default: true to disable)
    public static final Long MIN_PUSH_DELAY = 5L; // minimal delay between two pushes in ms
}
