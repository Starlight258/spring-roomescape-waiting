package roomescape.presentation.web.member;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.application.member.MemberService;
import roomescape.domain.member.Member;
import roomescape.presentation.web.common.annotation.PreAuthorize;
import roomescape.presentation.web.member.dto.response.AdminMemberResponse;

@RestController
@RequestMapping("/admin/members")
@PreAuthorize("hasRole('ADMIN')")
public class AdminMemberController {

    private final MemberService memberService;

    public AdminMemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // ✅ 모든 회원 조회
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AdminMemberResponse>> getAllMembers() {

        List<Member> members = memberService.getAllMembers();

        List<AdminMemberResponse> responses = members.stream()
                .map(AdminMemberResponse::from)
                .toList();

        return ResponseEntity.ok(responses);
    }
}

