package roomescape.presentation.web.reservation;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.application.member.MemberService;
import roomescape.application.reservation.ReservationService;
import roomescape.application.reservation.dto.CreateReservationRequest;
import roomescape.domain.common.exceptions.NotFoundException;
import roomescape.domain.common.exceptions.UnAuthorizedException;
import roomescape.domain.member.Member;
import roomescape.domain.reservation.CancelResult;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationResult;
import roomescape.presentation.web.common.annotation.PreAuthorize;
import roomescape.presentation.web.common.response.ErrorResponse;
import roomescape.presentation.web.reservation.dto.request.CreateReservationHttpRequest;
import roomescape.presentation.web.reservation.dto.response.MyReservationResponse;
import roomescape.presentation.web.reservation.dto.response.ReservationResponse;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final MemberService memberService;

    public ReservationController(ReservationService reservationService, MemberService memberService) {
        this.reservationService = reservationService;
        this.memberService = memberService;
    }

    // ✅ 예약 생성 - Application Service에 위임
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createReservation(
            @RequestBody CreateReservationHttpRequest request,
            HttpServletRequest httpRequest) {

        try {
            // 1. 현재 로그인한 사용자 조회
            Member currentMember = getCurrentMember(httpRequest);

            // 2. HTTP 요청을 Application 요청으로 변환
            CreateReservationRequest serviceRequest = new CreateReservationRequest(
                    request.getName(),
                    request.getDate(),
                    request.getTimeId(),
                    request.getThemeId()
            );

            // 3. Application Service가 도메인 로직 처리
            ReservationResult result = reservationService.createReservation(serviceRequest, currentMember);

            // 4. 도메인 결과를 HTTP 응답으로 변환
            if (result.isSuccess()) {
                ReservationResponse response = ReservationResponse.from(result.getReservation());
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                ErrorResponse errorResponse = new ErrorResponse(result.getMessage());
                return ResponseEntity.badRequest().body(errorResponse);
            }

        } catch (UnAuthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("로그인이 필요합니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("예약 생성 중 오류가 발생했습니다."));
        }
    }

    // ✅ 내 예약 목록 조회
    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<MyReservationResponse>> getMyReservations(HttpServletRequest request) {

        try {
            Member currentMember = getCurrentMember(request);

            // Application Service가 도메인 컬렉션 활용
            List<Reservation> reservations = reservationService.getMyReservations(currentMember);

            // 도메인 객체를 응답 DTO로 변환
            List<MyReservationResponse> responses = reservations.stream()
                    .map(MyReservationResponse::from)
                    .toList();

            return ResponseEntity.ok(responses);

        } catch (UnAuthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // ✅ 예약 취소
    @DeleteMapping("/{reservationId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> cancelReservation(
            @PathVariable Long reservationId,
            HttpServletRequest request) {

        try {
            Member currentMember = getCurrentMember(request);

            // Application Service가 도메인 협력 조율
            CancelResult result = reservationService.cancelReservation(reservationId, currentMember);

            if (result.isSuccess()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse(result.getMessage()));
            }

        } catch (UnAuthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("로그인이 필요합니다."));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Helper method
    private Member getCurrentMember(HttpServletRequest request) {
        String email = extractEmailFromToken(request);
        return memberService.findMemberByEmail(email)
                .orElseThrow(() -> new UnAuthorizedException("유효하지 않은 사용자입니다."));
    }

    private String extractEmailFromToken(HttpServletRequest request) {
        // JWT 토큰에서 이메일 추출 로직
        String token = request.getHeader("Authorization");
        // 토큰 파싱 로직...
        return "user@example.com"; // 임시
    }
}

