package com.coastee.server.global.apipayload.exception.handler;

import com.coastee.server.global.apipayload.code.BaseErrorCode;
import com.coastee.server.global.apipayload.exception.GeneralException;

public class JsonException extends GeneralException {

    public JsonException(BaseErrorCode code) {
        super(code);
    }
}
