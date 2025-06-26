package roomescape.service.regular;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberName;
import roomescape.domain.member.MemberRole;
import roomescape.dto.request.member.LoginRequest;
import roomescape.dto.request.member.SignupRequest;
import roomescape.dto.response.member.CheckLoginResponse;
import roomescape.dto.response.member.SignupResponse;
import roomescape.exception.ConflictException;
import roomescape.exception.RoomescapeException;
import roomescape.exception.UnAuthorizedException;
import roomescape.repository.MemberRepository;

@Service
public class MemberService {

    private static final String TOKEN = "token";
    private static final String ROLE = "role";

    private final MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public SignupResponse signup(final SignupRequest request) {
        MemberName memberName = new MemberName(request.name());
        String email = request.email();
        validateDistinctName(memberName);
        validateDistinctEmail(email);
        Member savedMember = memberRepository.save(
                new Member(memberName, email, request.password(), MemberRole.REGULAR));
        return SignupResponse.from(savedMember);
    }

    public void login(final LoginRequest request, final HttpSession httpSession) {
        Member member = getMember(request.email());
        validatePassword(member.getPassword(), request.password());

        Long memberId = member.getId();
        httpSession.setAttribute(TOKEN, memberId);
        MemberRole role = member.getRole();
        httpSession.setAttribute(ROLE, role.name());
    }

    public CheckLoginResponse checkLogin(final HttpSession httpSession) {
        Long memberId = getMemberId(httpSession);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RoomescapeException("Server state cannot be reached"));
        return CheckLoginResponse.from(member);
    }

    private void validateDistinctName(final MemberName name) {
        if (memberRepository.existsByName(name)) {
            throw new ConflictException("Member name is already exist");
        }
    }

    private void validateDistinctEmail(final String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new ConflictException("Member email is already exist");
        }
    }

    private Member getMember(final String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new UnAuthorizedException("Member email is not exist"));
    }

    private void validatePassword(final String correctPassword, final String comparedPassword) {
        if (!correctPassword.equals(comparedPassword)) {
            throw new UnAuthorizedException("Password is not correct");
        }
    }

    private Long getMemberId(final HttpSession httpSession) {
        Long memberId = (Long) httpSession.getAttribute(TOKEN);
        if (memberId == null || !memberRepository.existsById(memberId)) {
            throw new UnAuthorizedException("You are not logged in");
        }
        return memberId;
    }
}
