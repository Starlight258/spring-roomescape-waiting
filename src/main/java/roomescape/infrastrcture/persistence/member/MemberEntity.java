package roomescape.infrastrcture.persistence.member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import roomescape.domain.member.Email;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberName;
import roomescape.domain.member.MemberRole;
import roomescape.domain.member.Password;

@Entity
@Table(name = "members")
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 10)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole role;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    // 기본 생성자 (JPA 필수)
    protected MemberEntity() {
    }

    // 생성자
    public MemberEntity(String email, String password, String name, MemberRole role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.createdAt = LocalDateTime.now();
    }

    // ID 포함 생성자 (restore용)
    public MemberEntity(Long id, String email, String password, String name,
                        MemberRole role, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.createdAt = createdAt;
    }

    // ✅ 도메인 객체로 변환
    public Member toDomain() {
        return Member.restore(
                id,
                new Email(email),
                Password.encoded(password),
                new MemberName(name),
                role,
                createdAt
        );
    }

    // ✅ 도메인 객체에서 생성
    public static MemberEntity from(Member member) {
        if (member.getId() != null) {
            // ID가 있는 경우 (이미 저장된 회원)
            return new MemberEntity(
                    member.getId(),
                    member.getEmail().getValue(),
                    member.getPassword().getValue(),
                    member.getName().getValue(),
                    member.getRole(),
                    member.getCreatedAt()
            );
        } else {
            // ID가 없는 경우 (새로 생성하는 회원)
            return new MemberEntity(
                    member.getEmail().getValue(),
                    member.getPassword().getValue(),
                    member.getName().getValue(),
                    member.getRole()
            );
        }
    }

    // getters and setters
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public MemberRole getRole() {
        return role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRole(MemberRole role) {
        this.role = role;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
