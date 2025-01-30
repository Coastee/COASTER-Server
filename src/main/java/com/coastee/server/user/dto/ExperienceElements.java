package com.coastee.server.user.dto;

import com.coastee.server.global.domain.PageInfo;
import com.coastee.server.user.domain.Experience;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class ExperienceElements {
    private PageInfo pageInfo;
    private List<ExperienceElement> experienceList;

    public ExperienceElements(final Page<Experience> experiencePage) {
        this.pageInfo = new PageInfo(experiencePage);
        this.experienceList = experiencePage.getContent().stream().map(ExperienceElement::new).toList();
    }
}
