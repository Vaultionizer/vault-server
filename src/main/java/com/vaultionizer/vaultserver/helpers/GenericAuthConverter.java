package com.vaultionizer.vaultserver.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaultionizer.vaultserver.model.dto.GenericAuthDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class GenericAuthConverter implements Converter<String, GenericAuthDto> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public GenericAuthDto convert(String json) {
        try {
            var auth = objectMapper.readValue(json, GenericAuthDto.class);
            if (auth == null || auth.getUserID() == null || auth.getSessionKey() == null || auth.getSessionKey().isBlank())
                return null;
            return auth;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
