package com.vaultionizer.vaultserver.model.dto;

public class GetVersionResponseDto {
    private String version;
    private String maintainer;

    public GetVersionResponseDto(String version, String maintainer) {
        this.version = version;
        this.maintainer = maintainer;
    }

    public String getVersion() {
        return version;
    }

    public String getMaintainer() {
        return maintainer;
    }
}
