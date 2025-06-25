package roomescape.controller.admin;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.RequireRole;
import roomescape.domain.member.MemberRole;
import roomescape.dto.response.waiting.WaitingRetrievalResponse;
import roomescape.service.admin.AdminWaitingService;

@RestController
@RequestMapping("/admin/waitings")
@RequireRole(role = MemberRole.ADMIN)
public class AdminReservationWaitingController {

    private final AdminWaitingService waitingService;

    public AdminReservationWaitingController(final AdminWaitingService waitingService) {
        this.waitingService = waitingService;
    }

    @GetMapping
    public List<WaitingRetrievalResponse> findAll() {
        return waitingService.findAll();
    }
}
