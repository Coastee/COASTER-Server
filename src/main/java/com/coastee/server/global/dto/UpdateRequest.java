package com.coastee.server.global.dto;

public abstract class UpdateRequest {

    protected  <T> T getOrDefault(
            final T newValue,
            final T defaultValue
    ) {
        if (newValue != null)
            return newValue;
        return defaultValue;
    }
}
