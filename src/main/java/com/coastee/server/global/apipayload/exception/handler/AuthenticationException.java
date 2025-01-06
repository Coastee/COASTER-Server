package com.coastee.server.global.apipayload.exception.handler;

import com.coastee.server.global.apipayload.code.BaseErrorCode;
import com.coastee.server.global.apipayload.exception.GeneralException;

public class AuthenticationException extends GeneralException {
    public AuthenticationException(BaseErrorCode code) {
        super(code);
    }
}
