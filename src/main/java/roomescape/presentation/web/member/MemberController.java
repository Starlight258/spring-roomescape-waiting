package roomescape.presentation.web.member;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.application.member.MemberService;
import roomescape.application.member.dto.LoginRequest;
import roomescape.domain.member.LoginResult;
import roomescape.domain.member.Member;
import roomescape.presentation.util.AuthenticationUtil;
import roomescape.presentation.web.common.annotation.PreAuthorize;
import roomescape.presentation.web.common.response.ErrorResponse;
import roomescape.presentation.web.member.dto.response.LoginHttpRequest;
import roomescape.presentation.web.member.dto.response.LoginResponse;
import roomescape.presentation.web.member.dto.response.MemberResponse;
import roomescape.presentation.security.JwtTokenProvider;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberController(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginHttpRequest request) {

        try {
            LoginRequest serviceRequest = new LoginRequest(request.getEmail(), request.getPassword());
            LoginResult result = memberService.login(serviceRequest);

            if (result.isSuccess()) {
                // JWT 토큰 생성
                String token = jwtTokenProvider.createToken(result.getMember());

                // 응답 생성
                LoginResponse response = LoginResponse.from(result.getMember(), token);

                return ResponseEntity.ok()
                        .header("Set-Cookie", createTokenCookie(token))
                        .body(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorResponse(result.getMessage()));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("로그인 중 오류가 발생했습니다."));
        }
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MemberResponse> getCurrentUser(HttpServletRequest request) {
        Member currentMember = AuthenticationUtil.getCurrentMember(request);
        MemberResponse response = MemberResponse.from(currentMember);
        return ResponseEntity.ok(response);
    }

    private String createTokenCookie(String token) {
        return String.format("token=%s; HttpOnly; Path=/; Max-Age=3600; SameSite=Strict", token);
    }
}
