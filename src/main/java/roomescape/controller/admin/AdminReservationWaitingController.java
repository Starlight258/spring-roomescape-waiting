package roomescape.controller.admin;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.RequireRole;
import roomescape.domain.member.MemberRole;
import roomescape.dto.response.reservation.ReservationPreservationResponse;
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

    @DeleteMapping("/{id}")
    public ResponseEntity<ReservationPreservationResponse> remove(
            final @PathVariable Long id
    ) {
        waitingService.remove(id);
        return ResponseEntity.noContent().build();
    }
}
