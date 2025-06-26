package roomescape.dto.response.reservationtime;

import java.time.LocalTime;
import roomescape.domain.reservationtime.ReservationTime;

public record ReservationTimePreservationResponse(Long id, LocalTime startAt) {

    public static ReservationTimePreservationResponse from(final ReservationTime reservationTime) {
        return new ReservationTimePreservationResponse(reservationTime.getId(),
                reservationTime.getStartAt());
    }
}
