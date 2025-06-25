package roomescape.dto.response.member;

import roomescape.domain.member.Member;

public record MemberRetrievalResponse(Long id, String name) {

    public static MemberRetrievalResponse from(final Member member) {
        return new MemberRetrievalResponse(member.getId(), member.getName().getName());
    }
}
