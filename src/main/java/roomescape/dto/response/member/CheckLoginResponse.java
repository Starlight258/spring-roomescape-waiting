package roomescape.dto.response.member;

import roomescape.domain.member.Member;

public record CheckLoginResponse(String name) {

    public static CheckLoginResponse from(final Member member) {
        return new CheckLoginResponse(member.getName().getName());
    }
}
