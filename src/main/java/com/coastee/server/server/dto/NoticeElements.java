package com.coastee.server.server.dto;

import com.coastee.server.global.domain.PageInfo;
import com.coastee.server.server.domain.Notice;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class NoticeElements {
    private PageInfo pageInfo;
    private List<NoticeElement> noticeList;

    public NoticeElements(final Page<Notice> noticePage) {
        this.pageInfo = new PageInfo(noticePage);
        this.noticeList = noticePage.getContent().stream().map(NoticeElement::new).toList();
    }
}
