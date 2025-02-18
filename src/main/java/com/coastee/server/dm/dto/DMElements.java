package com.coastee.server.dm.dto;

import com.coastee.server.dm.domain.DirectMessage;
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
public class DMElements {
    private PageInfo pageInfo;
    private List<DMElement> dmList;

    public DMElements(final Page<DirectMessage> dmPage) {
        this.pageInfo = new PageInfo(dmPage);
        this.dmList = dmPage.stream().map(DMElement::new).toList();
    }
}
