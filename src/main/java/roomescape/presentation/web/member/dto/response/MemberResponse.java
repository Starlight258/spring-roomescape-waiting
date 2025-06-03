package roomescape.presentation.web.member.dto.response;

import java.time.LocalDateTime;
import roomescape.domain.member.Member;

public class MemberResponse {

    private final Long id;
    private final String email;
    private final String name;
    private final String role;
    private final LocalDateTime createdAt;

    public MemberResponse(Long id, String email, String name, String role, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.role = role;
        this.createdAt = createdAt;
    }

    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getEmail().getValue(),
                member.getName().getValue(),
                member.getRole().name(),
                member.getCreatedAt()
        );
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
