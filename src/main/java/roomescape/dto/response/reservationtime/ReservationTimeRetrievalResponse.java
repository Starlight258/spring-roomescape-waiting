package roomescape.dto.response.reservationtime;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.domain.reservationtime.ReservationTime;

public record ReservationTimeRetrievalResponse(
        Long id,
        @JsonFormat(pattern = "HH:mm")
        LocalTime startAt
) {

    public static ReservationTimeRetrievalResponse from(ReservationTime reservationTime) {
        return new ReservationTimeRetrievalResponse(reservationTime.getId(), reservationTime.getStartAt());
    }
}
