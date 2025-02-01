package com.coastee.server.chat.dto;

import com.coastee.server.chat.domain.Chat;
import com.coastee.server.global.domain.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class ChatElements {
    private PageInfo pageInfo;
    private List<ChatElement> chatList;

    public ChatElements(Page<Chat> chatPage) {
        this.pageInfo = new PageInfo(chatPage);
        this.chatList = chatPage.getContent().stream().map(ChatElement::new).toList();
    }
}
