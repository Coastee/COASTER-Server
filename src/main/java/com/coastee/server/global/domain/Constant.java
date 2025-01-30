package com.coastee.server.global.domain;

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
    public static final String CHANNEL_NAME = "chatroom";
    // chatroom
    public static final int MAX_COUNT = 1000;
}
