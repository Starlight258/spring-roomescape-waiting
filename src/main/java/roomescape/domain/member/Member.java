package roomescape.domain.member;

import java.time.LocalDateTime;
import java.util.Objects;
import roomescape.domain.reservation.Reservation;

public class Member {

    private Long id;
    private final Email email;
    private final Password password;
    private final MemberName name;
    private final MemberRole role;
    private LocalDateTime createdAt;

    // 생성자 (팩토리 메서드를 통해서만 생성)
    private Member(Email email, Password password, MemberName name, MemberRole role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.createdAt = LocalDateTime.now();
    }

    public static Member restore(Long id, Email email, Password password,
                                 MemberName name, MemberRole role, LocalDateTime createdAt) {
        Member member = new Member(email, password, name, role);
        member.id = id;  // ID 설정 (리플렉션이나 별도 설정 필요)
        member.createdAt = createdAt;  // 생성일 복원
        return member;
    }

    // 팩토리 메서드 - 회원가입
    public static SignUpResult signUp(String email, String rawPassword, String name,
                                      PasswordEncoder encoder, EmailDuplicationChecker checker) {
        try {
            Email memberEmail = new Email(email);

            // 이메일 중복 체크
            if (checker.isDuplicated(memberEmail)) {
                return SignUpResult.failure("이미 사용 중인 이메일입니다.");
            }

            // 비밀번호 암호화
            Password encodedPassword = Password.encode(rawPassword, encoder);
            MemberName memberName = new MemberName(name);

            Member member = new Member(memberEmail, encodedPassword, memberName, MemberRole.USER);
            return SignUpResult.success(member);

        } catch (IllegalArgumentException e) {
            return SignUpResult.failure(e.getMessage());
        }
    }

    // 관리자 생성 팩토리 메서드
    public static SignUpResult createAdmin(String email, String rawPassword, String name,
                                           PasswordEncoder encoder, EmailDuplicationChecker checker) {
        try {
            Email memberEmail = new Email(email);

            if (checker.isDuplicated(memberEmail)) {
                return SignUpResult.failure("이미 사용 중인 이메일입니다.");
            }

            Password encodedPassword = Password.encode(rawPassword, encoder);
            MemberName memberName = new MemberName(name);

            Member admin = new Member(memberEmail, encodedPassword, memberName, MemberRole.ADMIN);
            return SignUpResult.success(admin);

        } catch (IllegalArgumentException e) {
            return SignUpResult.failure(e.getMessage());
        }
    }

    // 로그인 인증
    public LoginResult authenticate(String rawPassword, PasswordEncoder encoder) {
        if (password.matches(rawPassword, encoder)) {
            return LoginResult.success(this);
        }
        return LoginResult.failure("이메일 또는 비밀번호가 일치하지 않습니다.");
    }

    // 권한 확인 메서드들
    public boolean canAccessAdminFeatures() {
        return role.canAccessAdminFeatures();
    }

    public boolean canManageReservations() {
        return role.canManageReservations();
    }

    public boolean canManageThemes() {
        return role.canManageThemes();
    }

    public boolean canManageTimes() {
        return role.canManageTimes();
    }

    // 예약 권한 확인 (예약 도메인과 협력)
    public boolean canCancelReservation(Reservation reservation) {
        // 본인 예약이거나 관리자인 경우
        return reservation.isOwnedBy(this) || this.canManageReservations();
    }

    // 이메일 일치 확인
    public boolean hasSameEmail(Email email) {
        return this.email.isSame(email);
    }

    // 동일 회원 확인
    public boolean isSameMember(Member other) {
        return this.id != null && this.id.equals(other.id);
    }

    // 회원 정보 업데이트 (이름만 변경 가능)
    public Member updateName(String newName) {
        MemberName updatedName = new MemberName(newName);
        return new Member(this.email, this.password, updatedName, this.role);
    }

    // getters
    public Long getId() {
        return id;
    }

    public Email getEmail() {
        return email;
    }

    public MemberName getName() {
        return name;
    }

    public Password getPassword() {
        return password;
    }

    public MemberRole getRole() {
        return role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Member member = (Member) obj;
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
