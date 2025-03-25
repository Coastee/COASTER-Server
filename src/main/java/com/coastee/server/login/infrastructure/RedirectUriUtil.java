package com.coastee.server.login.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RedirectUriUtil {
    @Value("${login.linkedin.client-id}")
    private String linkedinClientId;

    @Value("${login.linkedin.callback-uri}")
    private String linkedinCallbackUri;

    public String getHomeUri(final Long serverId) {
        return "https://localhost:5173/" + serverId + "/home";
    }

    public String getLinkedinRedirectUri() {
        return "https://www.linkedin.com/oauth/v2/authorization" +
                "?response_type=code" +
                "&client_id=" + linkedinClientId +
                "&redirect_uri=" + linkedinCallbackUri +
                "&state=STATE_STRING&scope=profile%20email%20openid";
    }

    public String getProfileUri(final Long userId) {
        return "https://coasterchat.com/mypage/" + userId;
    }
}
