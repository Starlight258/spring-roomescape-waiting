package roomescape.dto.response.reservationtime;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;

public record ReservationTimeAvailableResponse(
        @JsonFormat(pattern = "HH:mm")
        LocalTime startAt,
        Long timeId,
        boolean alreadyBooked
) {
}
