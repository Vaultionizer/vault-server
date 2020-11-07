package com.vaultionizer.vaultserver.model.dto;

public class FileUploadWebsocketDto {
    private GenericAuthDto auth;
    private Long spaceID;
    private Long saveIndex;
    private String fileContent;

    public FileUploadWebsocketDto(GenericAuthDto auth, Long spaceID, Long saveIndex, String fileContent) throws IllegalAccessException {
        if (auth == null || auth.getSessionKey() == null || auth.getUserID() == null ||
                spaceID == null || saveIndex == null || fileContent == null) {
            throw new IllegalArgumentException("Fileupload contents not valid.");
        }
        this.auth = auth;
        this.spaceID = spaceID;
        this.saveIndex = saveIndex;
        this.fileContent = fileContent;
    }


    public GenericAuthDto getAuth() {
        return auth;
    }

    public Long getSpaceID() {
        return spaceID;
    }

    public Long getSaveIndex() {
        return saveIndex;
    }

    public String getFileContent() {
        return fileContent;
    }
}
