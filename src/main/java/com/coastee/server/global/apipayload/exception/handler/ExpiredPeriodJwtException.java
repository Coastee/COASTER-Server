package com.coastee.server.global.apipayload.exception.handler;

import com.coastee.server.global.apipayload.code.BaseErrorCode;
import com.coastee.server.global.apipayload.exception.GeneralException;

public class ExpiredPeriodJwtException extends GeneralException {

    public ExpiredPeriodJwtException(BaseErrorCode code) {
        super(code);
    }
}
