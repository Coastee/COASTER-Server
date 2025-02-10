package com.coastee.server.dm.dto.request;

import com.coastee.server.chat.domain.ChatType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class DMRequest {
    private Long roomId;
    private String content;
    private ChatType type;
}
