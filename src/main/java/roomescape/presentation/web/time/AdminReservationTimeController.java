package roomescape.presentation.web.time;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.application.time.ReservationTimeService;
import roomescape.application.time.dto.CreateTimeRequest;
import roomescape.domain.common.exceptions.NotFoundException;
import roomescape.domain.time.TimeCreateResult;
import roomescape.domain.time.TimeDeleteResult;
import roomescape.presentation.web.common.annotation.PreAuthorize;
import roomescape.presentation.web.common.response.ErrorResponse;
import roomescape.presentation.web.time.dto.request.CreateTimeHttpRequest;
import roomescape.presentation.web.time.dto.response.TimeResponse;

@RestController
@RequestMapping("/admin/times")
@PreAuthorize("hasRole('ADMIN')")
public class AdminReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public AdminReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    // ✅ 예약 시간 생성 - Application Service가 도메인 팩토리 활용
    @PostMapping
    public ResponseEntity<?> createTime(@RequestBody CreateTimeHttpRequest request) {

        CreateTimeRequest serviceRequest = new CreateTimeRequest(request.getTimeString());

        // Application Service가 ReservationTime.create() 활용
        TimeCreateResult result = reservationTimeService.createTime(serviceRequest);

        if (result.isSuccess()) {
            TimeResponse response = TimeResponse.from(result.getReservationTime());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(result.getMessage()));
        }
    }

    // ✅ 예약 시간 삭제 - Application Service가 도메인 객체 협력 조율
    @DeleteMapping("/{timeId}")
    public ResponseEntity<?> deleteTime(@PathVariable Long timeId) {

        try {
            // Application Service가 ReservationTime.checkDeletable() 활용
            TimeDeleteResult result = reservationTimeService.deleteTime(timeId);

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
}

