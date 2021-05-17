package com.vaultionizer.vaultserver.model.dto.wserrors;

public class UploadData extends WSErrorData {
    private final Long userID;
    private final Long spaceID;
    private final Long saveIndex;
    private final String sessionKey;

    public UploadData(Long userID, Long spaceID, Long saveIndex, String sessionKey) {
        this.userID = userID;
        this.spaceID = spaceID;
        this.saveIndex = saveIndex;
        this.sessionKey = sessionKey;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public Long getUserID() {
        return userID;
    }

    public Long getSpaceID() {
        return spaceID;
    }

    public Long getSaveIndex() {
        return saveIndex;
    }
}
