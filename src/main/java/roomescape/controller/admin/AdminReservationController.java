package roomescape.controller.admin;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.request.reservation.AdminReservationPreservationRequest;
import roomescape.dto.response.reservation.ReservationPreservationResponse;
import roomescape.service.admin.AdminReservationService;

@RestController
@RequestMapping("/admin/reservations")
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
}
