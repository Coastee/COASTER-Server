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
    INVALID_OAUTH_TOKEN(UNAUTHORIZED, "LOGIN4001", "유효하지 않은 접근 토큰입니다."),
    INVALID_REFRESH_TOKEN(BAD_REQUEST, "LOGIN4002", "유효하지 않은 재발급 토큰입니다."),
    NULL_TOKEN(UNAUTHORIZED, "LOGIN4002", "토큰이 존재하지 않습니다."),
    EXPIRED_ACCESS_TOKEN(UNAUTHORIZED, "LOGIN4003", "만료된 액세스 토큰입니다."),
    EXPIRED_REFRESH_TOKEN(UNAUTHORIZED, "LOGIN4004", "만료된 리프레시 토큰입니다."),
    FAIL_VALIDATE_TOKEN(BAD_REQUEST, "LOGIN4005", "토큰 유효성 검사 중 오류가 발생했습니다."),

    // Session
    FAIL_VALIDATE_SESSION(UNAUTHORIZED, "SESSION4001", "세션 유효성 검사 중 오류가 발생하였습니다. 다시 시도하세요."),

    // User
    INVALID_USER_ID(BAD_REQUEST, "USER4001", "유효하지 않은 유저의 아이디입니다."),

    // Experience
    INVALID_EXPERIENCE_ID(BAD_REQUEST, "EXPERIENCE4001", "유효하지 않은 경험 아이디입니다."),

    // Server
    INVALID_SERVER_ID(BAD_REQUEST, "SERVER4001", "유효하지 않은 서버 아이디입니다."),
    NOT_IN_SERVER(BAD_REQUEST, "SERVER4002", "현재 유저는 이 서버에 참여하고 있지 않습니다."),

    // Chat
    INVALID_CHAT_ID(BAD_REQUEST, "CHAT4001", "유효하지 않은 채팅 아이디입니다."),

    // Chatroom
    INVALID_CHATROOM_ID(BAD_REQUEST, "CHATROOM4001", "유효하지 않은 채팅방 아이디입니다."),
    MAX_PARTICIPANT(BAD_REQUEST, "CHATROOM4002", "최대 참여자 수에 도달하여 더 이상 참여할 수 없는 채팅방입니다."),
    NOT_IN_CHATROOM(BAD_REQUEST, "CHATROOM4003", "현재 유저는 이 채팅방에 참여하고 있지 않습니다."),
    FAIL_CREATE_CHATROOM(BAD_REQUEST, "CHATROOM4004", "채팅방 생성 과정에 실패하였습니다. 요청 엔드포인트가 잘못되었습니다."),

    FAIL_FIND_SERVER_CHATROOM(INTERNAL_SERVER_ERROR, "CHATROOM5001", "서버 전체 채팅방 부재, 관리자에게 문의 바랍니다."),

    // DM
    INVALID_DM_ID(BAD_REQUEST, "DM4001", "유효하지 않은 DM 아이디입니다."),
    BAD_DM_REQUEST(BAD_REQUEST, "DM4002", "필수 전달 요소가 부족합니다."),

    // DM room
    INVALID_DMROOM_ID(BAD_REQUEST, "DMROOM4001", "유효하지 않은 DM 채팅방 아이디입니다."),
    NOT_IN_DMROOM(BAD_REQUEST, "DMROOM4002", "현재 유저는 이 DM 채팅방에 참여하고 있지 않습니다."),


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
