package roomescape.presentation.web.member.dto.response;

import roomescape.domain.member.Member;

public class MemberInfo {

    private final Long id;
    private final String name;

    public MemberInfo(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MemberInfo from(Member member) {
        return new MemberInfo(
                member.getId(),
                member.getName().getValue()
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

