package com.coastee.server.hashtag.dto;

import com.coastee.server.global.domain.PageInfo;
import com.coastee.server.hashtag.domain.HashTag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class HashTagElements {
    private PageInfo pageInfo;
    private List<HashTagElement> hashTagList;

    public HashTagElements(final Page<HashTag> hashTagPage) {
        this.pageInfo = new PageInfo(hashTagPage);
        this.hashTagList = hashTagPage.getContent().stream().map(HashTagElement::new).toList();
    }
}
