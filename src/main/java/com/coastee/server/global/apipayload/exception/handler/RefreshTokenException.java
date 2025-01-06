package com.coastee.server.global.apipayload.exception.handler;

import com.coastee.server.global.apipayload.code.BaseErrorCode;
import com.coastee.server.global.apipayload.exception.GeneralException;

public class RefreshTokenException extends GeneralException {

    public RefreshTokenException(BaseErrorCode code) {
        super(code);
    }
}
