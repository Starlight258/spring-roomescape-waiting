package roomescape.controller.regular;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.request.member.LoginRequest;
import roomescape.dto.request.member.SignupRequest;
import roomescape.dto.response.member.CheckLoginResponse;
import roomescape.dto.response.member.SignupResponse;
import roomescape.service.regular.MemberService;

@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/members")
    public ResponseEntity<SignupResponse> signup(final @RequestBody @Valid SignupRequest request) {
        SignupResponse response = memberService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(final @RequestBody @Valid LoginRequest request, final HttpSession httpSession) {
        memberService.login(request, httpSession);
        return ResponseEntity
                .ok()
                .build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(final HttpSession httpSession) {
        httpSession.invalidate();
        return ResponseEntity
                .ok()
                .build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<CheckLoginResponse> checkLogin(final HttpSession httpSession) {
        CheckLoginResponse response = memberService.checkLogin(httpSession);
        return ResponseEntity.ok().body(response);
    }
}
