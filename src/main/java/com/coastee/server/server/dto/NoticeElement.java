package com.coastee.server.server.dto;

import com.coastee.server.server.domain.Notice;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class NoticeElement {
    private Long id;
    private String title;
    private String content;

    public NoticeElement(final Notice notice) {
        this.id = notice.getId();
        this.title = notice.getTitle();
        this.content = notice.getContent();
    }
}
