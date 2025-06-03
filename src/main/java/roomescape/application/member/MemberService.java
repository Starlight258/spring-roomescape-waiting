package roomescape.application.member;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.application.member.dto.LoginRequest;
import roomescape.application.member.dto.SignUpRequest;
import roomescape.domain.member.Email;
import roomescape.domain.member.LoginResult;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRepository;
import roomescape.domain.member.SignUpResult;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberFactory memberFactory;
    private final AuthenticationService authenticationService;

    public MemberService(MemberRepository memberRepository,
                         MemberFactory memberFactory,
                         AuthenticationService authenticationService) {
        this.memberRepository = memberRepository;
        this.memberFactory = memberFactory;
        this.authenticationService = authenticationService;
    }

    // ✅ 회원가입 - 도메인 팩토리 활용
    public SignUpResult signUp(SignUpRequest request) {
        // 도메인 팩토리가 비즈니스 로직 담당
        SignUpResult result = memberFactory.createUser(
                request.getEmail(),
                request.getPassword(),
                request.getName()
        );

        if (result.isSuccess()) {
            memberRepository.save(result.getMember());
        }

        return result;
    }

    // ✅ 로그인 - 도메인 서비스 활용
    @Transactional(readOnly = true)
    public LoginResult login(LoginRequest request) {
        try {
            Email email = new Email(request.getEmail());
            Optional<Member> memberOpt = memberRepository.findByEmail(email);

            if (memberOpt.isEmpty()) {
                return LoginResult.failure("이메일 또는 비밀번호가 일치하지 않습니다.");
            }

            // 도메인 서비스가 인증 로직 담당
            return authenticationService.authenticate(memberOpt.get(), request.getPassword());

        } catch (IllegalArgumentException e) {
            return LoginResult.failure("올바르지 않은 이메일 형식입니다.");
        }
    }

    // ✅ 모든 회원 조회 (관리자용)
    @Transactional(readOnly = true)
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    // ✅ 회원 찾기
    @Transactional(readOnly = true)
    public Optional<Member> findMemberByEmail(String email) {
        try {
            Email memberEmail = new Email(email);
            return memberRepository.findByEmail(memberEmail);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}

