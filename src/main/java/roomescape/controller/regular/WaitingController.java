package roomescape.controller.regular;

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
import roomescape.dto.request.member.MemberPrinciple;
import roomescape.dto.request.waiting.WaitingPreservationRequest;
import roomescape.dto.response.reservation.ReservationPreservationResponse;
import roomescape.dto.response.waiting.WaitingPreservationResponse;
import roomescape.service.regular.WaitingService;

@RestController
@RequestMapping("/waiting")
public class WaitingController {

    private final WaitingService waitingService;

    public WaitingController(final WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    @RequireRole
    @PostMapping
    public ResponseEntity<WaitingPreservationResponse> create(
            final @RequestBody @Valid WaitingPreservationRequest request,
            final MemberPrinciple memberPrinciple
    ) {
        WaitingPreservationResponse response = waitingService.create(request, memberPrinciple);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @RequireRole
    @DeleteMapping("/{id}")
    public ResponseEntity<ReservationPreservationResponse> remove(
            final @PathVariable Long id,
            final MemberPrinciple memberPrinciple
    ) {
        waitingService.remove(id, memberPrinciple);
        return ResponseEntity.noContent().build();
    }
}
