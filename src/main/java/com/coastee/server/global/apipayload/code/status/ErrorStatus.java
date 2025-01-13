package com.coastee.server.global.apipayload.code.status;

import com.coastee.server.global.apipayload.code.BaseErrorCode;
import com.coastee.server.global.apipayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(UNAUTHORIZED, "COMMON401", "인증이 필요합니다. 권한을 확인해주세요."),
    _FORBIDDEN(FORBIDDEN, "COMMON403", "금지된 요청입니다."),
    _INVALID_AUTHORITY(UNAUTHORIZED, "COMMON401", "해당 리소스에 대한 접근 권한이 없습니다."),

    // Global
    IO_EXCEPTION(INTERNAL_SERVER_ERROR, "COMMON5001", "서버 IO Exception 발생, 관리자에게 문의 바랍니다"),
    JSON_EXCEPTION(INTERNAL_SERVER_ERROR, "COMMON5001", "서버 JSON Exception 발생, 관리자에게 문의 바랍니다"),

    // Login
    INVALID_OAUTH_TOKEN(UNAUTHORIZED, "LOGIN4001", "토큰을 가져올 수 없습니다."),
    NULL_TOKEN(UNAUTHORIZED, "LOGIN4002", "토큰이 존재하지 않습니다."),
    EXPIRED_ACCESS_TOKEN(UNAUTHORIZED, "LOGIN4003", "만료된 액세스 토큰입니다."),
    EXPIRED_REFRESH_TOKEN(UNAUTHORIZED, "LOGIN4004", "만료된 리프레시 토큰입니다."),
    FAIL_VALIDATE_TOKEN(BAD_REQUEST, "LOGIN4005", "토큰 유효성 검사 중 오류가 발생했습니다."),

    // User
    INVALID_USER_ID(BAD_REQUEST, "USER4001", "유효하지 않은 유저의 아이디입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}
