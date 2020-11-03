package com.vaultionizer.vaultserver.helpers;

import com.vaultionizer.vaultserver.model.dto.GetVersionResponseDto;

public class Config {
    public static final int MIN_USER_KEY_LENGTH = 64;

    public static final GetVersionResponseDto VERSION = new GetVersionResponseDto(
            "Vaultionizer v0.1",
            "No maintainer"
    );
}
