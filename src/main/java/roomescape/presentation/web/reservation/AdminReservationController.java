package roomescape.presentation.web.reservation;

import java.time.LocalDate;
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
import roomescape.application.reservation.ReservationService;
import roomescape.application.reservation.dto.CreateAdminReservationRequest;
import roomescape.application.reservation.dto.ReservationSearchRequest;
import roomescape.domain.common.exceptions.NotFoundException;
import roomescape.domain.reservation.CancelResult;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationResult;
import roomescape.presentation.web.common.annotation.PreAuthorize;
import roomescape.presentation.web.common.response.ErrorResponse;
import roomescape.presentation.web.reservation.dto.request.CreateAdminReservationHttpRequest;
import roomescape.presentation.web.reservation.dto.response.AdminReservationResponse;
import roomescape.presentation.web.reservation.dto.response.ReservationResponse;

@RestController
@RequestMapping("/admin/reservations")
@PreAuthorize("hasRole('ADMIN')")
public class AdminReservationController {

    private final ReservationService reservationService;

    public AdminReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // ✅ 모든 예약 조회
    @GetMapping
    public ResponseEntity<List<AdminReservationResponse>> getAllReservations(
            @RequestParam(required = false) Long themeId,
            @RequestParam(required = false) Long memberId,
            @RequestParam(required = false) LocalDate dateFrom,
            @RequestParam(required = false) LocalDate dateTo) {

        List<Reservation> reservations;

        // 검색 조건이 있으면 필터링, 없으면 전체 조회
        if (hasSearchConditions(themeId, memberId, dateFrom, dateTo)) {
            ReservationSearchRequest searchRequest = new ReservationSearchRequest(
                    themeId, memberId, dateFrom, dateTo
            );
            reservations = reservationService.searchReservations(searchRequest);
        } else {
            reservations = reservationService.getAllReservations();
        }

        List<AdminReservationResponse> responses = reservations.stream()
                .map(AdminReservationResponse::from)
                .toList();

        return ResponseEntity.ok(responses);
    }

    // ✅ 관리자 예약 생성
    @PostMapping
    public ResponseEntity<?> createAdminReservation(
            @RequestBody CreateAdminReservationHttpRequest request) {

        try {
            CreateAdminReservationRequest serviceRequest = new CreateAdminReservationRequest(
                    request.getDate(),
                    request.getTimeId(),
                    request.getThemeId(),
                    request.getMemberId()
            );

            ReservationResult result = reservationService.createAdminReservation(serviceRequest);

            if (result.isSuccess()) {
                ReservationResponse response = ReservationResponse.from(result.getReservation());
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse(result.getMessage()));
            }

        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    // ✅ 관리자 예약 취소
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<?> cancelAdminReservation(@PathVariable Long reservationId) {

        try {
            CancelResult result = reservationService.cancelAdminReservation(reservationId);

            if (result.isSuccess()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse(result.getMessage()));
            }

        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private boolean hasSearchConditions(Long themeId, Long memberId, LocalDate dateFrom, LocalDate dateTo) {
        return themeId != null || memberId != null || dateFrom != null || dateTo != null;
    }
}


