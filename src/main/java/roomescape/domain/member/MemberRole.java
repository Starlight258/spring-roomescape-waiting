package roomescape.domain.member;

import lombok.Getter;

@Getter
public enum MemberRole {

    REGULAR(0), ADMIN(1);

    private final int level;

    MemberRole(final int level) {
        this.level = level;
    }

    public boolean canAccess(final MemberRole comparedRole) {
        return this.level >= comparedRole.level;
    }
}
