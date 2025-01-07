package com.coastee.server.global;

public class Constant {
    // auth
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String HEADER_REFRESH_TOKEN = "RefreshToken";
    public static final String AUTHORITIES_KEY = "role";
    // api
    public static final int DEFAULT_PAGING_SIZE = 30;
    // redis
    public static final String CHANNEL_NAME = "chatroom";
}
