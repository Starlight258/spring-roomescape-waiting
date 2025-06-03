package roomescape.application.member;

import org.springframework.stereotype.Component;
import roomescape.domain.member.EmailDuplicationChecker;
import roomescape.domain.member.Member;
import roomescape.domain.member.PasswordEncoder;
import roomescape.domain.member.SignUpResult;

@Component
public class MemberFactory {

    private final PasswordEncoder passwordEncoder;
    private final EmailDuplicationChecker emailDuplicationChecker;

    public MemberFactory(PasswordEncoder passwordEncoder,
                         EmailDuplicationChecker emailDuplicationChecker) {
        this.passwordEncoder = passwordEncoder;
        this.emailDuplicationChecker = emailDuplicationChecker;
    }

    public SignUpResult createUser(String email, String password, String name) {
        // 도메인 팩토리 메서드 호출
        return Member.signUp(email, password, name, passwordEncoder, emailDuplicationChecker);
    }
}

