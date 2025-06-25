package roomescape.controller.admin;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.RequireRole;
import roomescape.domain.member.MemberRole;
import roomescape.dto.request.reservation.AdminReservationPreservationRequest;
import roomescape.dto.response.reservation.ReservationPreservationResponse;
import roomescape.service.admin.AdminReservationService;

@RestController
@RequestMapping("/admin/reservations")
@RequireRole(role = MemberRole.ADMIN)
public class AdminReservationController {

    private final AdminReservationService reservationService;

    public AdminReservationController(final AdminReservationService adminReservationService) {
        this.reservationService = adminReservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationPreservationResponse> create(
            final @RequestBody @Valid AdminReservationPreservationRequest request
    ) {
        ReservationPreservationResponse response = reservationService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ReservationPreservationResponse> remove(
            final @PathVariable Long id
    ) {
        reservationService.remove(id);
        return ResponseEntity.noContent().build();
    }
}
