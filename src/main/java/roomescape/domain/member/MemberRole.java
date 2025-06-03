package roomescape.domain.member;

public enum MemberRole {

    USER("일반 사용자"),
    ADMIN("관리자");

    private final String description;

    MemberRole(String description) {
        this.description = description;
    }

    public boolean canAccessAdminFeatures() {
        return this == ADMIN;
    }

    public boolean canManageReservations() {
        return this == ADMIN;
    }

    public boolean canManageThemes() {
        return this == ADMIN;
    }

    public boolean canManageTimes() {
        return this == ADMIN;
    }

    public String getDescription() {
        return description;
    }
}

