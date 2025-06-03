package roomescape.application.member;

import org.springframework.stereotype.Component;
import roomescape.domain.member.LoginResult;
import roomescape.domain.member.Member;
import roomescape.domain.member.PasswordEncoder;

@Component
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResult authenticate(Member member, String rawPassword) {
        // 도메인 객체가 인증 로직 담당
        return member.authenticate(rawPassword, passwordEncoder);
    }
}
