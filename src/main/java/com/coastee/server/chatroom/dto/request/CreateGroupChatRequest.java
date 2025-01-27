package com.coastee.server.chatroom.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class CreateGroupChatRequest {
    @Size(min = 1, max = 20, message = "제목은 최대 1자 이상, 20자 이하로 설정할 수 있습니다.")
    private String title;

    @Size(max = 150, message = "내용은 최대 150자 이하로 설정할 수 있습니다.")
    private String content;
}
