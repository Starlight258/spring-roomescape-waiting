package roomescape.dto.response.member;

import roomescape.domain.member.Member;

public record SignupResponse(Long id) {

    public static SignupResponse from(final Member member) {
        return new SignupResponse(member.getId());
    }
}
