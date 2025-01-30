package com.coastee.server.chatroom.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class CreateGroupChatRequest {
    @Size(min = 1, max = 20, message = "제목은 최대 1자 이상, 20자 이하로 설정할 수 있습니다.")
    private String title;

    @Size(max = 80, message = "상세 설명은 최대 80자 이하로 설정할 수 있습니다.")
    private String content;

    @Size(max = 10, message = "해시 태그는 최대 10개까지 추가할 수 있습니다.")
    private Set<String> hashTags;
}
