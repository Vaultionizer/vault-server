package com.vaultionizer.vaultserver.model.dto;

public class GetVersionResponseDto {
    private String version;
    private String maintainer;
    private boolean hasAuthKey;

    public GetVersionResponseDto(String version, String maintainer, boolean hasAuthKey) {
        this.version = version;
        this.maintainer = maintainer;
        this.hasAuthKey = hasAuthKey;
    }

    public String getVersion() {
        return version;
    }

    public String getMaintainer() {
        return maintainer;
    }

    public boolean isHasAuthKey() {
        return hasAuthKey;
    }
}
