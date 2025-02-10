package com.coastee.server.global.domain;

import java.time.LocalDateTime;

public class Constant {
    // auth
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final int TOKEN_PREFIX_LENGTH = 7;
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String HEADER_REFRESH_TOKEN = "RefreshToken";
    public static final String AUTHORITIES_KEY = "role";
    // api
    public static final int DEFAULT_PAGING_SIZE = 40;
    // redis
    public static final String CHATROOM_CHANNEL_NAME = "chatroom";
    public static final String DM_CHANNEL_NAME = "dm";
    public static final String CHATROOM_LISTENER_METHOD = "sendChat";
    public static final String DM_LISTENER_METHOD = "sendDM";
    // chatroom
    public static final int MAX_COUNT = 1000;
    public static final int UNLIMITED = Integer.MAX_VALUE;
    // experience
    public static final LocalDateTime CURRENT_DATE = LocalDateTime.of(999999999, 12, 31, 23, 59, 59);
}
