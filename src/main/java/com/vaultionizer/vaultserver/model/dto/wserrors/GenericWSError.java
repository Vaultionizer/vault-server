package com.vaultionizer.vaultserver.model.dto.wserrors;

public class GenericWSError {
    private final WS_ERROR type;
    private final WSErrorData data;

    public GenericWSError(WS_ERROR type, WSErrorData data) {
        this.type = type;
        this.data = data;
    }

    public WS_ERROR getType() {
        return type;
    }

    public WSErrorData getData() {
        return data;
    }
}
