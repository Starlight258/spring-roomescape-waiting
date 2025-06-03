package roomescape.presentation.web.time;

import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.application.time.ReservationTimeService;
import roomescape.domain.time.ReservationTime;
import roomescape.domain.time.TimeDisplayInfo;
import roomescape.presentation.web.theme.dto.response.AvailableTimeResponse;
import roomescape.presentation.web.time.dto.response.TimeResponse;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    // ✅ 모든 예약 시간 조회
    @GetMapping
    public ResponseEntity<List<TimeResponse>> getAllTimes() {

        // Application Service가 ReservationTimes 컬렉션으로 정렬
        List<ReservationTime> times = reservationTimeService.getAllTimes();

        List<TimeResponse> responses = times.stream()
                .map(TimeResponse::from)
                .toList();

        return ResponseEntity.ok(responses);
    }

    // ✅ 예약 가능한 시간 조회 - Application Service가 도메인 컬렉션 활용
    @GetMapping("/available")
    public ResponseEntity<List<AvailableTimeResponse>> getAvailableTimes(
            @RequestParam LocalDate date,
            @RequestParam Long themeId) {

        // Application Service가 ReservationTimes로 가용성 체크
        List<TimeDisplayInfo> availableTimes = reservationTimeService.getAvailableTimes(date, themeId);

        List<AvailableTimeResponse> responses = availableTimes.stream()
                .map(AvailableTimeResponse::from)
                .toList();

        return ResponseEntity.ok(responses);
    }
}

