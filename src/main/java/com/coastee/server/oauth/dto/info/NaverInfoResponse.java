package com.coastee.server.oauth.dto.info;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverInfoResponse {
    @JsonProperty("resultcode")
    private String resultCode;

    private String message;

    @JsonProperty("response")
    private NaverInfoDetail infoDetail;
}

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
class NaverInfoDetail {
    @JsonProperty("id")
    private String socialId;
    private String name;
    private String email;
}
