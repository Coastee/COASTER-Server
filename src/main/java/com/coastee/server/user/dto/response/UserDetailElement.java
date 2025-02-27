package com.coastee.server.user.dto.response;

import com.coastee.server.user.domain.Experience;
import com.coastee.server.user.domain.User;
import com.coastee.server.user.dto.ExperienceElements;
import com.coastee.server.user.dto.UserElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class UserDetailElement extends UserElement {
    private String bio;
    private List<String> urlList;
    private Long dmRoomId;
    private ExperienceElements experience;

    @Builder(builderMethodName = "from")
    public UserDetailElement(
            final User user,
            final Long dmRoomId,
            final Page<Experience> experiencePage
    ) {
        super(user);
        this.bio = user.getBio();
        this.urlList = user.getUrlList();
        this.dmRoomId = dmRoomId;
        this.experience = new ExperienceElements(experiencePage);
    }
}
