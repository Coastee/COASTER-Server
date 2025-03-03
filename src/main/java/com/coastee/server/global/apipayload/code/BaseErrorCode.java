package com.coastee.server.global.apipayload.code;

public interface BaseErrorCode {

    public String getCode();
    public ErrorReasonDTO getReason();
    public ErrorReasonDTO getReasonHttpStatus();
}
