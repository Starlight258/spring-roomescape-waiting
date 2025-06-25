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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.RequireRole;
import roomescape.domain.member.MemberRole;
import roomescape.dto.request.reservationtime.ReservationTimePreservationRequest;
import roomescape.dto.response.reservation.ReservationPreservationResponse;
import roomescape.dto.response.reservationtime.ReservationTimeAvailableResponse;
import roomescape.dto.response.reservationtime.ReservationTimePreservationResponse;
import roomescape.dto.response.reservationtime.ReservationTimeRetrievalResponse;
import roomescape.service.regular.ReservationTimeService;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(final ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @RequireRole(role = MemberRole.ADMIN)
    @PostMapping
    public ResponseEntity<ReservationTimePreservationResponse> create(
            final @RequestBody @Valid ReservationTimePreservationRequest request) {
        ReservationTimePreservationResponse response = reservationTimeService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public List<ReservationTimeRetrievalResponse> findAll() {
        return reservationTimeService.findAll();
    }

    @GetMapping("/available")
    public List<ReservationTimeAvailableResponse> findAllAvailable(
            @RequestParam String date, @RequestParam Long themeId) {
        return reservationTimeService.findAllAvailable(date, themeId);
    }

    @RequireRole(role = MemberRole.ADMIN)
    @DeleteMapping("/{id}")
    public ResponseEntity<ReservationPreservationResponse> remove(final @PathVariable Long id) {
        reservationTimeService.remove(id);
        return ResponseEntity.noContent().build();
    }
}
