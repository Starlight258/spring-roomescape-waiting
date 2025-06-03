package roomescape.presentation.web.member.dto.response;

import java.time.LocalDateTime;
import roomescape.domain.member.Member;

public class AdminMemberResponse {

    private final Long id;
    private final String email;
    private final String name;
    private final String role;
    private final LocalDateTime createdAt;
    private final int reservationCount;

    public AdminMemberResponse(Long id, String email, String name, String role,
                               LocalDateTime createdAt, int reservationCount) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.role = role;
        this.createdAt = createdAt;
        this.reservationCount = reservationCount;
    }

    public static AdminMemberResponse from(Member member) {
        return new AdminMemberResponse(
                member.getId(),
                member.getEmail().getValue(),
                member.getName().getValue(),
                member.getRole().name(),
                member.getCreatedAt(),
                0  // 실제로는 예약 개수 계산 필요
        );
    }

    public static AdminMemberResponse from(Member member, int reservationCount) {
        return new AdminMemberResponse(
                member.getId(),
                member.getEmail().getValue(),
                member.getName().getValue(),
                member.getRole().name(),
                member.getCreatedAt(),
                reservationCount
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

    public int getReservationCount() {
        return reservationCount;
    }
}

