package com.coastee.server.login.infrastructure.loginparams;

import com.coastee.server.login.domain.OAuthLoginParams;
import com.coastee.server.user.domain.SocialType;
import lombok.Data;

@Data
public class GoogleLoginParams implements OAuthLoginParams {
    private String grant_type = GRANT_TYPE;
    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;

    @Override
    public SocialType socialType() {
        return SocialType.GOOGLE;
    }

    public GoogleLoginParams(
            final String code
    ) {
        this.code = code;
    }

    @Override
    public void updateClientId(final String clientId) {
        this.client_id = clientId;
    }

    @Override
    public void updateClientSecret(final String clientSecret) {
        this.client_secret = clientSecret;
    }

    @Override
    public void updateRedirectUri(final String uri) {
        this.redirect_uri = uri;
    }
}
