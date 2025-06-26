package roomescape.controller.regular;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.RequireRole;
import roomescape.dto.request.member.MemberPrinciple;
import roomescape.dto.request.reservation.RegularReservationPreservationRequest;
import roomescape.dto.response.reservation.MyReservationRetrievalResponse;
import roomescape.dto.response.reservation.ReservationPreservationResponse;
import roomescape.dto.response.reservation.ReservationRetrievalResponse;
import roomescape.service.regular.ReservationService;

@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(final ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @RequireRole
    @PostMapping("/reservations")
    public ResponseEntity<ReservationPreservationResponse> create(
            final @RequestBody @Valid RegularReservationPreservationRequest request,
            final MemberPrinciple memberPrinciple
    ) {
        ReservationPreservationResponse response = reservationService.create(request, memberPrinciple);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/reservations")
    public List<ReservationRetrievalResponse> findAll() {
        return reservationService.findAll();
    }

    @RequireRole
    @GetMapping("/reservations-mine")
    public List<MyReservationRetrievalResponse> findMyReservations(
            final MemberPrinciple memberPrinciple
    ) {
        return reservationService.findMyReservations(memberPrinciple);
    }

    @RequireRole
    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<ReservationPreservationResponse> remove(
            final @PathVariable Long id,
            final MemberPrinciple memberPrinciple
    ) {
        reservationService.remove(id, memberPrinciple);
        return ResponseEntity.noContent().build();
    }
}
